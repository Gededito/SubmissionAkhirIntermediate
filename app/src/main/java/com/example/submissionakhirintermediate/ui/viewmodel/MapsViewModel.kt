package com.example.submissionakhirintermediate.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.submissionakhirintermediate.data.model.UserModel
import com.example.submissionakhirintermediate.data.repository.StoryRepository

class MapsViewModel(private val repository: StoryRepository): ViewModel() {

    fun getLocationStory(token: String) = repository.getLocationStory(token)

    fun getUser(): LiveData<UserModel> {
        return repository.getUserData()
    }

}