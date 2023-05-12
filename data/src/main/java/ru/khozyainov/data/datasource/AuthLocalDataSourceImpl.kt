package ru.khozyainov.data.datasource

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.khozyainov.data.local.RddtDataStore
import ru.khozyainov.data.models.TokenEntity
import java.util.UUID

class AuthLocalDataSourceImpl(
    private val dataStore: RddtDataStore
) : AuthLocalDataSource {

    override fun getToken(): Flow<TokenEntity> =
        dataStore.value.data.map { preferences ->
            TokenEntity(accessToken = preferences[ACCESS_TOKEN] ?: "")
        }

    override suspend fun setToken(tokenEntity: TokenEntity): Boolean =
        withContext(Dispatchers.IO){
            dataStore.value.edit { preferences ->
                preferences[ACCESS_TOKEN] = tokenEntity.accessToken
            }
            true
        }

    override fun getDeviceIDFlow(): Flow<String> = dataStore.value.data.map { preferences ->
        preferences[DEVICE_ID] ?: createDeviceID()
    }

    private suspend fun createDeviceID(): String {
        val deviceId = UUID.randomUUID().toString()
        dataStore.value.edit { preferences ->
            preferences[DEVICE_ID] = deviceId
        }
        return deviceId
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        private val DEVICE_ID = stringPreferencesKey("DEVICE_ID")
    }
}