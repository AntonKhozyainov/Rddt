package ru.khozyainov.rddt.di

import org.koin.dsl.module
import ru.khozyainov.data.local.RddtDataStore
import ru.khozyainov.data.datasource.AuthLocalDataSource
import ru.khozyainov.data.datasource.AuthLocalDataSourceImpl
import ru.khozyainov.data.datasource.AuthRemoteDataSource
import ru.khozyainov.data.datasource.AuthRemoteDataSourceImpl
import ru.khozyainov.data.datasource.OnboardingLocalDataSource
import ru.khozyainov.data.datasource.OnboardingLocalDataSourceImpl
import ru.khozyainov.data.datasource.PostSortTypeLocalDataSource
import ru.khozyainov.data.datasource.PostSortTypeLocalDataSourceImpl
import ru.khozyainov.data.mapper.OnboardingMapper
import ru.khozyainov.data.mapper.PostSortTypeMapper
import ru.khozyainov.data.mapper.TokenMapper
import ru.khozyainov.data.network.AuthService
import ru.khozyainov.data.network.AuthorizationFailedInterceptor
import ru.khozyainov.data.network.AuthorizationInterceptor
import ru.khozyainov.data.repo.AuthRepositoryImpl
import ru.khozyainov.data.repo.OnboardingRepositoryImpl
import ru.khozyainov.data.repo.PostRepositoryImpl
import ru.khozyainov.domain.repo.AuthRepository
import ru.khozyainov.domain.repo.OnboardingRepository
import ru.khozyainov.domain.repo.PostRepository

val dataModule = module {

    single {
        RddtDataStore(context = get())
    }

    single {
        AuthService(context = get())
    }

    //Mapper
    single {
        OnboardingMapper()
    }

    single {
        TokenMapper()
    }

    single {
        PostSortTypeMapper()
    }

    //DataSource
    single<OnboardingLocalDataSource> {
        OnboardingLocalDataSourceImpl(dataStore = get())
    }

    single<AuthLocalDataSource> {
        AuthLocalDataSourceImpl(dataStore = get())
    }

    single<AuthRemoteDataSource> {
        AuthRemoteDataSourceImpl(authService = get())
    }

    single<PostSortTypeLocalDataSource> {
        PostSortTypeLocalDataSourceImpl(dataStore = get())
    }

    //Repository
    single<OnboardingRepository> {
        OnboardingRepositoryImpl(onboardingLocalDataSource = get(), onboardingMapper = get())
    }

    single<AuthRepository> {
        AuthRepositoryImpl(authLocalDataSource = get(), authRemoteDataSource = get(), tokenMapper = get())
    }

    single<PostRepository> {
        PostRepositoryImpl(postSortTypeLocalDataSource = get(), postSortTypeMapper = get())
    }

    //Interceptor
    single {
        AuthorizationFailedInterceptor(authLocalDataSource = get(), authService = get())
    }

    single {
        AuthorizationInterceptor(authLocalDataSource = get())
    }

}