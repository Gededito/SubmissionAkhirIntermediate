package com.example.submissionakhirintermediate.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.submissionakhirintermediate.AuthenticationDummy
import com.example.submissionakhirintermediate.DataUserDummy
import com.example.submissionakhirintermediate.data.model.LoginResponse
import com.example.submissionakhirintermediate.data.repository.StoryRepository
import com.example.submissionakhirintermediate.utils.MainDispatcherRule
import com.example.submissionakhirintermediate.utility.Result
import com.example.submissionakhirintermediate.utils.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get: Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var repository: StoryRepository
    private lateinit var loginViewModel: LoginViewModel
    private val auth = AuthenticationDummy.provideLoginResponse()
    private val email = "dito999dito@gmail.com"
    private val password = "6723823"

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(repository)
    }

    @Test
    fun `if login success then return Success`() {
        val exceptedLiveData = MutableLiveData<Result<LoginResponse>>()
        exceptedLiveData.value = Result.Success(auth)

        val expected = exceptedLiveData.getOrAwaitValue()

        `when`(repository.loginUser(email, password)).thenReturn(exceptedLiveData)
        val actual = loginViewModel.loginUser(email, password).getOrAwaitValue()

        verify(repository).loginUser(email, password)
        assertNotNull(actual)
        assertTrue(actual is Result.Success)
        assertEquals(expected, actual)

    }

    @Test
    fun `if network error then return Async Error`() {
        val expectedLiveData = MutableLiveData<Result<LoginResponse>>(Result.Error("Dummy"))
        `when`(repository.loginUser(email, password)).thenReturn(expectedLiveData)

        val actual = loginViewModel.loginUser(email, password).getOrAwaitValue()
        verify(repository).loginUser(email, password)
        assertTrue(actual is Result.Error)
        assertNotNull(actual)
    }

    @Test
    fun `save user story`() = runTest {
        loginViewModel.saveUser(DataUserDummy.generateDummyLoginUser())
        verify(repository).saveUserData(DataUserDummy.generateDummyLoginUser())
    }

    @Test
    fun `logout user` () = runTest{
        loginViewModel.logout()
        verify(repository).logout()
    }

}