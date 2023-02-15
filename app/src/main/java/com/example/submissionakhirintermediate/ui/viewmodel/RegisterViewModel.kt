package com.example.submissionakhirintermediate.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.submissionakhirintermediate.data.repository.StoryRepository

class RegisterViewModel(private val storyRepository: StoryRepository): ViewModel() {

    fun registerUser(name: String, email: String, password: String ) =
        storyRepository.registerUser(name, email, password)

}