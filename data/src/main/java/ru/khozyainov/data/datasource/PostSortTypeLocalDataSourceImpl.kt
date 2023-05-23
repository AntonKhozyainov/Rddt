package ru.khozyainov.data.datasource

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.khozyainov.data.local.datastore.RddtDataStore
import ru.khozyainov.data.models.PostSortTypeEntity

class PostSortTypeLocalDataSourceImpl(
    private val dataStore: RddtDataStore
): PostSortTypeLocalDataSource {

    override fun getSortType(): Flow<PostSortTypeEntity> {
        return dataStore.value.data.map {preferences ->
            PostSortTypeEntity(sortType = preferences[POST_SORT_TYPE] ?: "HOT")
        }
    }

    override suspend fun saveSortType(sortType: PostSortTypeEntity) {
        dataStore.value.edit { preferences ->
            preferences[POST_SORT_TYPE] = sortType.sortType
        }
    }

    companion object {
        private val POST_SORT_TYPE = stringPreferencesKey("POST_SORT_TYPE")
    }
}