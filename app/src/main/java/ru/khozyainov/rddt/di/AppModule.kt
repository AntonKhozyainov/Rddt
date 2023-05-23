package ru.khozyainov.rddt.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.khozyainov.domain.usecase.feed.GetPostsUseCase
import ru.khozyainov.rddt.mapper.UiPostSortTypeMapper
import ru.khozyainov.rddt.ui.feed.FeedViewModel
import ru.khozyainov.rddt.ui.launcher.LauncherViewModel
import ru.khozyainov.rddt.ui.login.LoginViewModel
import ru.khozyainov.rddt.ui.onboarding.OnboardingViewModel

val appModule = module {

    //ViewModel
    viewModel{
        LauncherViewModel(
            getOnboardingStateUseCase = get(),
            getLoginStateUseCase = get()
        )
    }

    viewModel{
        OnboardingViewModel(
            onboardingViewedUseCase = get()
        )
    }

    viewModel {
        LoginViewModel(
            getLoginIntentUseCase = get(),
            getAndSaveTokenByRequestUseCase = get()
        )
    }

    viewModel {
        FeedViewModel(
            getPostSortTypeUseCase = get(),
            setPostSortTypeUseCase = get(),
            getPostsUseCase = get(),
            uiPostSortTypeMapper = get()
        )
    }

    //Mapper
    single {
        UiPostSortTypeMapper()
    }
}