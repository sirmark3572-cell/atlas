package com.sirmark.goldcapsule

data class GoldQuote(
    val price: Double,
    val previousClose: Double,
    val currency: String,
    val timestamps: List<Long>,
    val closes: List<Double>,
    val fetchedAt: Long = System.currentTimeMillis()
) {
    val change: Double get() = price - previousClose
    val changePercent: Double
        get() = if (previousClose == 0.0) 0.0 else change / previousClose * 100.0
}
