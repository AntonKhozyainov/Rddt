package ru.khozyainov.rddt

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import ru.khozyainov.rddt.di.appModule
import ru.khozyainov.rddt.di.dataModule
import ru.khozyainov.rddt.di.domainModule

class RddtApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@RddtApplication)
            modules(listOf(appModule, dataModule, domainModule))
        }
    }
}