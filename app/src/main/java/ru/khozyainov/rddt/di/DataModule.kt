package ru.khozyainov.rddt.di

import org.koin.dsl.module
import ru.khozyainov.data.RddtDataStore
import ru.khozyainov.data.datasource.AuthLocalDataSource
import ru.khozyainov.data.datasource.AuthLocalDataSourceImpl
import ru.khozyainov.data.datasource.OnboardingLocalDataSource
import ru.khozyainov.data.datasource.OnboardingLocalDataSourceImpl
import ru.khozyainov.data.mapper.OnboardingMapper
import ru.khozyainov.data.mapper.TokenMapper
import ru.khozyainov.data.repo.AuthRepositoryImpl
import ru.khozyainov.data.repo.OnboardingRepositoryImpl
import ru.khozyainov.domain.repo.AuthRepository
import ru.khozyainov.domain.repo.OnboardingRepository

val dataModule = module {

    single {
        RddtDataStore(context = get())
    }

    //Mapper
    single {
        OnboardingMapper()
    }

    single {
        TokenMapper()
    }

    //DataSource
    single<OnboardingLocalDataSource> {
        OnboardingLocalDataSourceImpl(dataStore = get())
    }

    single<AuthLocalDataSource> {
        AuthLocalDataSourceImpl(dataStore = get())
    }

    //Repository
    single<OnboardingRepository> {
        OnboardingRepositoryImpl(onboardingLocalDataSource = get(), onboardingMapper = get())
    }

    single<AuthRepository> {
        AuthRepositoryImpl(authLocalDataSource = get(), tokenMapper = get())
    }
}