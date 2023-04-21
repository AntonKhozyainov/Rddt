package ru.khozyainov.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.khozyainov.domain.model.Onboarding

interface OnboardingRepository {
    fun getOnboardingState(): Flow<Onboarding>
    suspend fun setOnboardingState(onboarding: Onboarding)
}