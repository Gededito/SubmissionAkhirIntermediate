package com.example.submissionakhirintermediate.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.submissionakhirintermediate.data.model.UserModel
import com.example.submissionakhirintermediate.data.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {

    fun storyAdd(token: String, file: MultipartBody.Part, description: RequestBody,
        lat: Double?, lon: Double?) =
        storyRepository.storyAdd(token, file, description, lat, lon)

    fun getUser(): LiveData<UserModel> {
        return storyRepository.getUserData()
    }

}