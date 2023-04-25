package ru.khozyainov.data.local

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile

class RddtDataStore(
    context: Application
) {
    val value: DataStore<Preferences> = PreferenceDataStoreFactory.create {
        context.preferencesDataStoreFile("data_store")
    }
}