package ru.khozyainov.rddt.di

import org.koin.dsl.module
import ru.khozyainov.domain.usecase.login.GetLoginStateUseCase
import ru.khozyainov.domain.usecase.onboarding.GetOnboardingStateUseCase

val domainModule = module {

    factory {
        GetOnboardingStateUseCase(onboardingRepository = get())
    }

    factory {
        GetLoginStateUseCase(authRepository = get())
    }
}