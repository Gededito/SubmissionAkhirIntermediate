package com.example.submissionakhirintermediate.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.submissionakhirintermediate.data.model.StoryApp
import com.example.submissionakhirintermediate.data.model.UserModel
import com.example.submissionakhirintermediate.data.repository.StoryRepository

class StoryViewModel (private val repository: StoryRepository): ViewModel() {

    fun getStory(): LiveData<PagingData<StoryApp>> {
        return repository.getStory().cachedIn(viewModelScope)
    }

    fun getUser(): LiveData<UserModel> {
        return repository.getUserData()
    }

}