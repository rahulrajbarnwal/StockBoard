package dev.rrb.stocks.storage

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.rrb.stocks.utils.Logger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first

data class SavedFilter(
    val name: String,
    val id: String,
    val type: String
)

object FilterPreferences {

    private val KEY_FILTER_NAME = stringPreferencesKey("filter_name")
    private val KEY_FILTER_ID = stringPreferencesKey("filter_id")
    private val KEY_FILTER_TYPE = stringPreferencesKey("filter_type")

    // Last filter the user picked, or null if none was saved (caller falls back to the default)
    suspend fun getSavedFilter(): SavedFilter? {
        return try {
            val prefs = AppDataStore.preferences.data.first()
            val name = prefs[KEY_FILTER_NAME]
            val id = prefs[KEY_FILTER_ID]
            val type = prefs[KEY_FILTER_TYPE]

            if (name.isNullOrBlank() || id.isNullOrBlank() || type.isNullOrBlank()) {
                null
            } else {
                SavedFilter(name = name, id = id, type = type)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.error("FilterPreferences", "Failed to read saved filter: ${e.message}", e)
            null
        }
    }

    suspend fun saveFilter(filter: SavedFilter) {
        try {
            AppDataStore.preferences.edit { prefs ->
                prefs[KEY_FILTER_NAME] = filter.name
                prefs[KEY_FILTER_ID] = filter.id
                prefs[KEY_FILTER_TYPE] = filter.type
            }
            Logger.debug("FilterPreferences", "Saved filter: $filter")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Logger.error("FilterPreferences", "Failed to save filter: ${e.message}", e)
        }
    }
}
