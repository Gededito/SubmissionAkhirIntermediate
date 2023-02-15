package com.example.submissionakhirintermediate.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.submissionakhirintermediate.utility.Result
import com.example.submissionakhirintermediate.api.ApiService
import com.example.submissionakhirintermediate.data.model.*
import com.example.submissionakhirintermediate.data.preferences.UserPreferences
import com.example.submissionakhirintermediate.ui.adapter.paging.StoryPagingSource
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val pref: UserPreferences, private val apiService: ApiService) {

    fun getStory(): LiveData<PagingData<StoryApp>> {
        return Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = {
                StoryPagingSource(apiService, pref)
            }
        ).liveData
    }

    fun loginUser(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.userLogin(LoginRequest(email, password))
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("Login", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    fun registerUser(name: String, email: String, password: String): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.userRegister(RegisterRequest(name, email, password))
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("Register", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    fun storyAdd(token: String, file: MultipartBody.Part, description: RequestBody, lat: Double?, lon: Double?):
            LiveData<Result<AddStoryResponse>> = liveData {

        emit(Result.Loading)
        try {
            val response = apiService.addStory(token, file, description, lat, lon)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("Register", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getLocationStory(token: String): LiveData<Result<StoryResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStoryLocation(token, 1)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("Register", e.message.toString())
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserData(): LiveData<UserModel> {
        return pref.getUserData().asLiveData()
    }

    suspend fun saveUserData(user: UserModel) {
        pref.saveUserData(user)
    }

//    suspend fun login() {
//        pref.login()
//    }

    suspend fun logout() {
        pref.logout()
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            preferences: UserPreferences,
            apiService: ApiService
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(preferences, apiService)
            }.also { instance = it }
    }

}