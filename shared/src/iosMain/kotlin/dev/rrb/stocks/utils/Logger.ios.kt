package dev.rrb.stocks.utils

actual object Logger {
    actual fun debug(tag: String, message: String) {
        println("DEBUG [$tag]: $message")
    }

    actual fun error(tag: String, message: String, throwable: Throwable?) {
        println("ERROR [$tag]: $message")
        throwable?.printStackTrace()
    }

    actual fun info(tag: String, message: String) {
        println("INFO [$tag]: $message")
    }
}