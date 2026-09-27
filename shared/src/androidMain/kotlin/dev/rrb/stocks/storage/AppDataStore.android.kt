package dev.rrb.stocks.storage

import android.content.Context

object AppContext {
    internal lateinit var application: Context
        private set

    fun init(context: Context) {
        application = context.applicationContext
    }
}

actual fun dataStoreFilePath(): String =
    AppContext.application.filesDir.resolve(DATASTORE_FILE_NAME).absolutePath
