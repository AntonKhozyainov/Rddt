package ru.khozyainov.rddt.di

import org.koin.dsl.module
import ru.khozyainov.data.local.datastore.RddtDataStore
import ru.khozyainov.data.datasource.AuthLocalDataSource
import ru.khozyainov.data.datasource.AuthLocalDataSourceImpl
import ru.khozyainov.data.datasource.AuthRemoteDataSource
import ru.khozyainov.data.datasource.AuthRemoteDataSourceImpl
import ru.khozyainov.data.datasource.OnboardingLocalDataSource
import ru.khozyainov.data.datasource.OnboardingLocalDataSourceImpl
import ru.khozyainov.data.datasource.PostPagingSource
import ru.khozyainov.data.datasource.PostPagingSourceImpl
import ru.khozyainov.data.datasource.PostSortTypeLocalDataSource
import ru.khozyainov.data.datasource.PostSortTypeLocalDataSourceImpl
import ru.khozyainov.data.local.db.RddtDatabase
import ru.khozyainov.data.mapper.OnboardingMapper
import ru.khozyainov.data.mapper.PostRequestArgsMapper
import ru.khozyainov.data.mapper.PostSortTypeMapper
import ru.khozyainov.data.mapper.TokenMapper
import ru.khozyainov.data.remote.AuthService
import ru.khozyainov.data.remote.interceptor.AuthorizationFailedInterceptor
import ru.khozyainov.data.remote.interceptor.AuthorizationInterceptor
import ru.khozyainov.data.remote.RddtService
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

    single {
        RddtService(
            authorizationInterceptor = get(),
            authorizationFailedInterceptor = get()
        )
    }

    single {
        RddtDatabase(context = get())
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

    single {
        PostRequestArgsMapper(postSortTypeMapper = get())
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

    single<PostPagingSource> {
        PostPagingSourceImpl(
            service = get(),
            database = get()
        )
    }
    //Repository
    single<OnboardingRepository> {
        OnboardingRepositoryImpl(
            onboardingLocalDataSource = get(),
            onboardingMapper = get()
        )
    }

    single<AuthRepository> {
        AuthRepositoryImpl(
            authLocalDataSource = get(),
            authRemoteDataSource = get(),
            tokenMapper = get()
        )
    }

    single<PostRepository> {
        PostRepositoryImpl(
            postPagingSource = get(),
            postSortTypeLocalDataSource = get(),
            postRequestArgsMapper = get(),
            postSortTypeMapper = get()
        )
    }

    //Interceptor
    single {
        AuthorizationFailedInterceptor(
            authLocalDataSource = get(),
            authService = get()
        )
    }

    single {
        AuthorizationInterceptor(authLocalDataSource = get())
    }

}