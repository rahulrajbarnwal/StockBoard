package dev.rrb.stocks.utils


fun Double.toFormattedPrice(): String {
    return formatDecimal(this, 2)
}

fun Double.toFormattedPercent(): String {
    return formatDecimal(this, 2)
}

// "2,79,43,274" -> "27.9M", "361000" -> "361K"; non-numeric input is returned unchanged
fun String.toCompactVolume(): String {
    val value = replace(",", "").trim().toDoubleOrNull() ?: return this
    val units = listOf(1_000_000_000.0 to "B", 1_000_000.0 to "M", 1_000.0 to "K")
    val (divisor, suffix) = units.firstOrNull { value >= it.first } ?: return value.toLong().toString()

    val scaled = value / divisor
    val text = if (scaled >= 100) {
        scaled.toLong().toString()
    } else {
        val tenths = (scaled * 10).toLong()
        if (tenths % 10 == 0L) (tenths / 10).toString() else "${tenths / 10}.${tenths % 10}"
    }
    return "$text$suffix"
}

private fun formatDecimal(value: Double, decimalPlaces: Int): String {
    if (value.isNaN() || value.isInfinite()) {
        return "0.00"
    }

    val sign = if (value < 0) "-" else ""
    val absValue = kotlin.math.abs(value)
    val multiplier = when (decimalPlaces) {
        1 -> 10
        2 -> 100
        3 -> 1000
        else -> 100
    }

    val rounded = (absValue * multiplier).toLong()
    val integerPart = rounded / multiplier
    val decimalPart = rounded % multiplier

    val decimalStr = decimalPart.toString().padStart(decimalPlaces, '0')

    return "$sign$integerPart.$decimalStr"
}