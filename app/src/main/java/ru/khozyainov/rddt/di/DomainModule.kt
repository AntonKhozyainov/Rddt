package ru.khozyainov.rddt.di

import org.koin.dsl.module
import ru.khozyainov.domain.usecase.login.GetLoginIntentUseCase
import ru.khozyainov.domain.usecase.login.GetLoginStateUseCase
import ru.khozyainov.domain.usecase.login.GetTokenByRequestUseCase
import ru.khozyainov.domain.usecase.onboarding.GetOnboardingStateUseCase
import ru.khozyainov.domain.usecase.onboarding.OnboardingViewedUseCase

val domainModule = module {

    factory {
        GetOnboardingStateUseCase(onboardingRepository = get())
    }

    factory {
        GetLoginStateUseCase(authRepository = get())
    }

    factory {
        OnboardingViewedUseCase(onboardingRepository = get())
    }

    factory {
        GetLoginIntentUseCase(authRepository = get())
    }

    factory {
        GetTokenByRequestUseCase(authRepository = get())
    }
}