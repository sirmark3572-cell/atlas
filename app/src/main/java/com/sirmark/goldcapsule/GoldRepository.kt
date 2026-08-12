package com.sirmark.goldcapsule

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

object GoldRepository {
    private const val ENDPOINT =
        "https://query1.finance.yahoo.com/v8/finance/chart/GC%3DF?range=1d&interval=5m"

    fun fetch(): GoldQuote {
        val connection = URL(ENDPOINT).openConnection() as HttpURLConnection
        try {
            connection.connectTimeout = 12_000
            connection.readTimeout = 12_000
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 GoldCapsule/1.0")

            if (connection.responseCode !in 200..299) {
                error("Quote service returned HTTP ${connection.responseCode}")
            }

            val root = connection.inputStream.bufferedReader().use { it.readText() }
            val result = JSONObject(root)
                .getJSONObject("chart")
                .getJSONArray("result")
                .getJSONObject(0)
            val meta = result.getJSONObject("meta")
            val timestampsJson = result.optJSONArray("timestamp")
            val closesJson = result
                .getJSONObject("indicators")
                .getJSONArray("quote")
                .getJSONObject(0)
                .optJSONArray("close")

            val timestamps = mutableListOf<Long>()
            val closes = mutableListOf<Double>()
            if (timestampsJson != null && closesJson != null) {
                for (index in 0 until minOf(timestampsJson.length(), closesJson.length())) {
                    if (!closesJson.isNull(index)) {
                        timestamps += timestampsJson.getLong(index)
                        closes += closesJson.getDouble(index)
                    }
                }
            }

            val price = meta.optDouble(
                "regularMarketPrice",
                closes.lastOrNull() ?: error("No gold price returned")
            )
            val previousClose = meta.optDouble("chartPreviousClose", price)
            return GoldQuote(
                price = price,
                previousClose = previousClose,
                currency = meta.optString("currency", "USD"),
                timestamps = timestamps,
                closes = closes
            )
        } finally {
            connection.disconnect()
        }
    }
}
