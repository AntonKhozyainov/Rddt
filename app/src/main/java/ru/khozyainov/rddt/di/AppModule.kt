package ru.khozyainov.rddt.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.khozyainov.rddt.ui.launcher.LauncherViewModel

val appModule = module {

    viewModel{
        LauncherViewModel(
            getOnboardingStateUseCase = get(),
            getLoginStateUseCase = get()
        )
    }

}