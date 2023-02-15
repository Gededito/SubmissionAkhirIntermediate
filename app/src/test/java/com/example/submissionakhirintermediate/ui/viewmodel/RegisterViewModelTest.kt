package com.example.submissionakhirintermediate.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.submissionakhirintermediate.AuthenticationDummy
import com.example.submissionakhirintermediate.data.model.RegisterResponse
import com.example.submissionakhirintermediate.data.repository.StoryRepository
import com.example.submissionakhirintermediate.utils.getOrAwaitValue
import com.example.submissionakhirintermediate.utility.Result
import org.junit.Assert
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {

    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: StoryRepository
    private lateinit var registerViewModel: RegisterViewModel
    private val auth = AuthenticationDummy.provideRegisterResponse()
    private val name = "gede"
    private val email = "dito999dito@gmail.com"
    private val password = "6723823"

    @Before
    fun setup() {
        registerViewModel = RegisterViewModel(repository)
    }

    @Test
    fun `when register called from repository it should return Success and not null`() {
        val excepted = MutableLiveData<Result<RegisterResponse>>()
        excepted.value = Result.Success(auth)
        `when`(repository.registerUser(name, email, password)).thenReturn(excepted)

        val actual = registerViewModel.registerUser(name, email, password).getOrAwaitValue()
        Assert.assertTrue(actual is Result.Success)
        Assert.assertNotNull(actual)
    }

    @Test
    fun `when register failed it should return Error and also not null`() {
        val expected = MutableLiveData<Result<RegisterResponse>>()
        expected.value = Result.Error("Something Error")
        `when`(repository.registerUser(name, email, password)).thenReturn(expected)

        val actual = registerViewModel.registerUser(name, email, password).getOrAwaitValue()
        Assert.assertTrue(actual is Result.Error)
        Assert.assertNotNull(actual)
    }

}