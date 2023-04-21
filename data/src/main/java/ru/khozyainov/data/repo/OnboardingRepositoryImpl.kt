package ru.khozyainov.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.khozyainov.data.datasource.OnboardingLocalDataSource
import ru.khozyainov.data.mapper.OnboardingMapper
import ru.khozyainov.domain.model.Onboarding
import ru.khozyainov.domain.repo.OnboardingRepository

class OnboardingRepositoryImpl(
    private val onboardingLocalDataSource: OnboardingLocalDataSource,
    private val onboardingMapper: OnboardingMapper
) : OnboardingRepository {

    override fun getOnboardingState(): Flow<Onboarding> =
        onboardingLocalDataSource.readOnBoardingState().map { onboardingEntity ->
            onboardingMapper.mapToDomain(entity = onboardingEntity)
        }

    override suspend fun setOnboardingState(onboarding: Onboarding) =
        onboardingLocalDataSource.saveOnBoardingState(
            onboardingMapper.mapToEntity(model = onboarding)
        )
}