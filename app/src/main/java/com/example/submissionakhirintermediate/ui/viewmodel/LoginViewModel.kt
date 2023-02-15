package com.example.submissionakhirintermediate.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.submissionakhirintermediate.data.model.UserModel
import com.example.submissionakhirintermediate.data.repository.StoryRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val storyRepository: StoryRepository): ViewModel() {

    fun loginUser(email: String, password: String) = storyRepository.loginUser(email, password)

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            storyRepository.saveUserData(user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }

}