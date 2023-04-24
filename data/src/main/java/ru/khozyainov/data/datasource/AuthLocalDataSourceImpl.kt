package ru.khozyainov.data.datasource

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.khozyainov.data.RddtDataStore
import ru.khozyainov.data.models.TokenEntity
import java.lang.Exception

class AuthLocalDataSourceImpl(
    private val dataStore: RddtDataStore
) : AuthLocalDataSource {

    override fun getToken(): Flow<TokenEntity> =
        dataStore.value.data.map { preferences ->
            TokenEntity(accessToken = preferences[ACCESS_TOKEN] ?: "")
        }

    override suspend fun setToken(tokenEntity: TokenEntity) {
        withContext(Dispatchers.IO){
            dataStore.value.edit { preferences ->
                preferences[ACCESS_TOKEN] = tokenEntity.accessToken
            }
        }
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
    }
}