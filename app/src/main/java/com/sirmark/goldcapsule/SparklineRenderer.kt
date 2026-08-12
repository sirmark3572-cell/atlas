package com.sirmark.goldcapsule

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path

object SparklineRenderer {
    fun render(values: List<Double>, rising: Boolean): Bitmap {
        val width = 220
        val height = 72
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        if (values.size < 2) return bitmap

        val min = values.minOrNull() ?: return bitmap
        val max = values.maxOrNull() ?: return bitmap
        val range = (max - min).takeIf { it > 0.0 } ?: 1.0
        val inset = 5f
        val path = Path()

        values.forEachIndexed { index, value ->
            val x = inset + index.toFloat() / (values.size - 1) * (width - inset * 2)
            val y = height - inset - ((value - min) / range).toFloat() * (height - inset * 2)
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        Canvas(bitmap).drawPath(path, Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = if (rising) Color.rgb(76, 217, 132) else Color.rgb(255, 92, 92)
            style = Paint.Style.STROKE
            strokeWidth = 4f
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        })
        return bitmap
    }
}
