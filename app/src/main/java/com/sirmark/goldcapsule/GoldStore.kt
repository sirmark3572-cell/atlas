package com.sirmark.goldcapsule

import android.content.Context

object GoldStore {
    private const val PREFS = "gold_quote"
    private const val PRICE = "price"
    private const val PREVIOUS = "previous"
    private const val CURRENCY = "currency"
    private const val FETCHED_AT = "fetched_at"
    private const val CLOSES = "closes"

    fun save(context: Context, quote: GoldQuote) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putLong(PRICE, quote.price.toRawBits())
            .putLong(PREVIOUS, quote.previousClose.toRawBits())
            .putString(CURRENCY, quote.currency)
            .putLong(FETCHED_AT, quote.fetchedAt)
            .putString(CLOSES, quote.closes.takeLast(96).joinToString(","))
            .apply()
    }

    fun load(context: Context): GoldQuote? {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        if (!prefs.contains(PRICE)) return null
        val closes = prefs.getString(CLOSES, "").orEmpty()
            .split(",")
            .mapNotNull(String::toDoubleOrNull)
        return GoldQuote(
            price = Double.fromBits(prefs.getLong(PRICE, 0)),
            previousClose = Double.fromBits(prefs.getLong(PREVIOUS, 0)),
            currency = prefs.getString(CURRENCY, "USD") ?: "USD",
            timestamps = emptyList(),
            closes = closes,
            fetchedAt = prefs.getLong(FETCHED_AT, 0)
        )
    }
}
