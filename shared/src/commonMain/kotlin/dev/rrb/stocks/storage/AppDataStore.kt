package dev.rrb.stocks.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

internal const val DATASTORE_FILE_NAME = "stocks.preferences_pb"

// Absolute path of the preferences file; each platform picks its app-private directory
expect fun dataStoreFilePath(): String

object AppDataStore {
    // DataStore allows only one instance per file, so it lives in a singleton
    val preferences: DataStore<Preferences> by lazy {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = { dataStoreFilePath().toPath() }
        )
    }
}
