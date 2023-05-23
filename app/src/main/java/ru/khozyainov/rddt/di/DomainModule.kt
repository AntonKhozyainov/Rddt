package ru.khozyainov.rddt.di

import org.koin.dsl.module
import ru.khozyainov.domain.usecase.feed.GetPostSortTypeUseCase
import ru.khozyainov.domain.usecase.feed.GetPostsUseCase
import ru.khozyainov.domain.usecase.feed.SetPostSortTypeUseCase
import ru.khozyainov.domain.usecase.login.GetLoginIntentUseCase
import ru.khozyainov.domain.usecase.login.GetLoginStateUseCase
import ru.khozyainov.domain.usecase.login.GetAndSaveTokenByRequestUseCase
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
        GetAndSaveTokenByRequestUseCase(authRepository = get())
    }

    factory {
        GetPostSortTypeUseCase(postRepository = get())
    }

    factory {
        SetPostSortTypeUseCase(postRepository = get())
    }

    factory {
        GetPostsUseCase(postRepository = get())
    }
}