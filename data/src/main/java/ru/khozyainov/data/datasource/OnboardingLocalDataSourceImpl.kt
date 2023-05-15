package ru.khozyainov.data.datasource

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import ru.khozyainov.data.local.RddtDataStore
import ru.khozyainov.data.models.OnboardingEntity

class OnboardingLocalDataSourceImpl(
    private val dataStore: RddtDataStore
) : OnboardingLocalDataSource {

    override fun readOnBoardingState(): Flow<OnboardingEntity> =
        dataStore.value.data.map { preferences ->
            OnboardingEntity(viewed = preferences[ONBOARDING_VIEWED] ?: false)
        }

    override suspend fun saveOnBoardingState(onboarding: OnboardingEntity) {
        dataStore.value.edit { preferences ->
            preferences[ONBOARDING_VIEWED] = onboarding.viewed
        }
    }

    companion object {
        private val ONBOARDING_VIEWED = booleanPreferencesKey("ONBOARDING_VIEWED")
    }
}