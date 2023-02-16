package com.example.submissionakhirintermediate.ui.adapter.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.submissionakhirintermediate.api.ApiService
import com.example.submissionakhirintermediate.data.model.StoryApp
import com.example.submissionakhirintermediate.data.preferences.UserPreferences
import kotlinx.coroutines.flow.first

class StoryPagingSource(private val apiService: ApiService, private val pref: UserPreferences):
    PagingSource<Int, StoryApp>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryApp> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val token = "Bearer ${pref.getUserData().first().token}"
            val responseData = apiService.getAllStory(token, page, params.loadSize).listStory

            LoadResult.Page(
                data = responseData,
                prevKey = if (page == INITIAL_PAGE_INDEX) null else page - 1,
                nextKey = if (responseData.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, StoryApp>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1)?: anchorPage?.nextKey?.minus(1)
        }
    }
    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}