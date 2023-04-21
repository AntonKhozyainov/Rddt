package ru.khozyainov.data.datasource

import kotlinx.coroutines.flow.Flow
import ru.khozyainov.data.models.OnboardingEntity

interface OnboardingLocalDataSource {
    fun readOnBoardingState(): Flow<OnboardingEntity>
    suspend fun saveOnBoardingState(onboarding: OnboardingEntity)
}