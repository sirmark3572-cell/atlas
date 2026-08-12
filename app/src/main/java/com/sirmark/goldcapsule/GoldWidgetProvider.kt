package com.sirmark.goldcapsule

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GoldWidgetProvider : AppWidgetProvider() {
    override fun onEnabled(context: Context) {
        GoldUpdateWorker.schedule(context)
        GoldUpdateWorker.refreshNow(context)
    }

    override fun onUpdate(
        context: Context,
        manager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { manager.updateAppWidget(it, buildViews(context)) }
        GoldUpdateWorker.schedule(context)
        GoldUpdateWorker.refreshNow(context)
    }

    companion object {
        fun updateAll(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val component = ComponentName(context, GoldWidgetProvider::class.java)
            manager.getAppWidgetIds(component).forEach {
                manager.updateAppWidget(it, buildViews(context))
            }
        }

        private fun buildViews(context: Context): RemoteViews {
            val views = RemoteViews(context.packageName, R.layout.widget_gold_capsule)
            val quote = GoldStore.load(context)

            if (quote == null) {
                views.setTextViewText(R.id.price, "—")
                views.setTextViewText(R.id.change, context.getString(R.string.loading))
                views.setViewVisibility(R.id.chart, View.INVISIBLE)
            } else {
                val rising = quote.change >= 0
                val sign = if (rising) "+" else ""
                views.setTextViewText(R.id.price, String.format(Locale.US, "%,.2f", quote.price))
                views.setTextViewText(
                    R.id.change,
                    String.format(
                        Locale.US,
                        "%s%.2f  (%s%.2f%%)",
                        sign,
                        quote.change,
                        sign,
                        quote.changePercent
                    )
                )
                views.setTextColor(
                    R.id.change,
                    if (rising) Color.rgb(76, 217, 132) else Color.rgb(255, 92, 92)
                )
                views.setTextViewText(
                    R.id.updated,
                    SimpleDateFormat("HH:mm", Locale.getDefault())
                        .format(Date(quote.fetchedAt))
                )
                views.setImageViewBitmap(
                    R.id.chart,
                    SparklineRenderer.render(quote.closes, rising)
                )
                views.setViewVisibility(R.id.chart, View.VISIBLE)
            }

            val openQuote = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://finance.yahoo.com/quote/GC=F/")
            )
            val pendingIntent = PendingIntent.getActivity(
                context,
                0,
                openQuote,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)
            return views
        }
    }
}
