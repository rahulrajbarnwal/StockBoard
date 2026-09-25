package dev.rrb.stocks.utils


fun Double.toFormattedPrice(): String {
    return formatDecimal(this, 2)
}

fun Double.toFormattedPercent(): String {
    return formatDecimal(this, 2)
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