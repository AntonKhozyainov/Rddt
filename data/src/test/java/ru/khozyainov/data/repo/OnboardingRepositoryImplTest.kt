package ru.khozyainov.data.repo

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever
import ru.khozyainov.data.datasource.OnboardingLocalDataSource
import ru.khozyainov.data.mapper.OnboardingMapper
import ru.khozyainov.data.models.OnboardingEntity
import ru.khozyainov.domain.model.Onboarding

@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingRepositoryImplTest {

    private val onboardingLocalDataSource = mock<OnboardingLocalDataSource>()
    private val onboardingMapper = OnboardingMapper()

    @After
    fun tearDown() {
        reset(onboardingLocalDataSource)
    }

    @Test
    fun `getOnboardingState should return correct onboarding`() = runTest{
        //given
        val expected = Onboarding(viewed = true)
        val onboardingEntity = OnboardingEntity(viewed = true)
        val repo = OnboardingRepositoryImpl(
            onboardingLocalDataSource = onboardingLocalDataSource,
            onboardingMapper = onboardingMapper
        )

        //when
        whenever(onboardingLocalDataSource.readOnBoardingState()).thenReturn(flow { emit(onboardingEntity) })
        val actual = repo.getOnboardingState().first()

        //then
        Assert.assertEquals(expected, actual)
    }


    @Test
    fun `setOnboardingState should run without error`() = runTest{
        //given
        val onboardin = Onboarding(viewed = true)
        val onboardingEntity = OnboardingEntity(viewed = true)
        val repo = OnboardingRepositoryImpl(
            onboardingLocalDataSource = onboardingLocalDataSource,
            onboardingMapper = onboardingMapper
        )

        //when
        whenever(onboardingLocalDataSource.saveOnBoardingState(onboarding = onboardingEntity)).thenReturn(Unit)
        repo.setOnboardingState(onboardin)

    }
}