package com.example.submissionakhirintermediate.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.submissionakhirintermediate.DataUserDummy
import com.example.submissionakhirintermediate.data.model.StoryResponse
import com.example.submissionakhirintermediate.data.model.UserModel
import com.example.submissionakhirintermediate.data.repository.StoryRepository
import com.example.submissionakhirintermediate.utility.Result
import com.example.submissionakhirintermediate.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: StoryRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val mapResponse = DataUserDummy.storyResponseLocation()
    private val token = ""

    @Before
    fun setup() {
        mapsViewModel = MapsViewModel(repository)
    }

    @Test
    fun `data should return the api with a location`() {
        val exceptedLiveData = MutableLiveData<Result<StoryResponse>>()
        exceptedLiveData.value = Result.Success(mapResponse)

        val excepted = exceptedLiveData.getOrAwaitValue()
        `when`(repository.getLocationStory(token)).thenReturn(exceptedLiveData)
        val actual = mapsViewModel.getLocationStory(token).getOrAwaitValue()

        Assert.assertNotNull(actual)
        assertTrue(actual is Result.Success)
        assertEquals(excepted, actual)
    }

    @Test
    fun `when get Story Location is Error`() {
        val expectedResponse = MutableLiveData<Result<StoryResponse>>()
        expectedResponse.value = Result.Error("error")

        `when`(repository.getLocationStory(token)).thenReturn(expectedResponse)

        val actualResponse = mapsViewModel.getLocationStory(token).getOrAwaitValue()

        Mockito.verify(repository).getLocationStory(token)
        Assert.assertNotNull(actualResponse)
        assertTrue(actualResponse is Result.Error)
    }

    @Test
    fun `test get user`(){
        val storyRepository = Mockito.mock(StoryRepository::class.java)
        val expectedLiveData = MutableLiveData<UserModel>()
        expectedLiveData.value = UserModel("tri", "abcd", true)

        `when`(storyRepository.getUserData()).thenReturn(expectedLiveData)
        val viewModel = MapsViewModel(storyRepository)
        val actualResult = viewModel.getUser().getOrAwaitValue()

        Assert.assertNotNull(actualResult)
        assertEquals(actualResult, expectedLiveData.value)
        assertEquals(actualResult.name, expectedLiveData.value?.name)
        assertEquals(actualResult.token, expectedLiveData.value?.token)
        assertEquals(actualResult.isLogin, expectedLiveData.value?.isLogin)
    }


}