package com.example.submissionakhirintermediate.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.submissionakhirintermediate.api.ApiConfig
import com.example.submissionakhirintermediate.data.preferences.UserPreferences
import com.example.submissionakhirintermediate.data.repository.StoryRepository

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storyapp")

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val preferences = UserPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(preferences, apiService)
    }
}