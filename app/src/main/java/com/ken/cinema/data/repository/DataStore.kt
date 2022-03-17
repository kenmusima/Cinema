package com.ken.cinema.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ken.cinema.util.SEATS_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStore @Inject constructor(context: Context) {
    private val Context.createDataStore:DataStore<Preferences> by preferencesDataStore(name = "seats")
    private val seatsID = stringPreferencesKey(SEATS_ID)
    private val dataStore = context.createDataStore

    suspend fun saveSeatsID(seatIds : String) {
        dataStore.edit { seats ->
            seats[seatsID] = seatIds
        }
    }

    val seatIdsFlow : Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[seatsID]
        }
}