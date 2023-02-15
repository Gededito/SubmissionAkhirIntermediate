package com.example.submissionakhirintermediate

import com.example.submissionakhirintermediate.data.model.AddStoryResponse
import com.example.submissionakhirintermediate.data.model.StoryApp
import com.example.submissionakhirintermediate.data.model.StoryResponse
import com.example.submissionakhirintermediate.data.model.UserModel

object DataUserDummy {

    fun addStoryResponse(): AddStoryResponse {
        return AddStoryResponse(false, "Ok")
    }

    fun storyResponseLocation(): StoryResponse {
        return StoryResponse(false, "Ok", generateDummyStoryLocation())
    }

    private fun generateDummyStoryLocation(): List<StoryApp> {
        val storyList = ArrayList<StoryApp>()
        for (i in 0..10) {
            val story = StoryApp(
                "ID",
                "gede",
                "desc",
                "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                "Thursday, October 20, 2022 at 3:03:44 PM Indochina Time",
                -6.1947508,
                106.4867868
            )
            storyList.add(story)
        }
        return storyList
    }

    fun generateDummyStoryList(): List<StoryApp> {
        val storyList = ArrayList<StoryApp>()
        for (i in 0..100) {
            val story = StoryApp(
                i.toString(),
                "name + $i",
                "desc + $i",
                "$i",
                "created + $i",
                i.toDouble(),
                i.toDouble()
            )
            storyList.add(story)
        }
        return storyList
    }

    fun generateDummyLoginUser(): UserModel {
        return UserModel(
            "tri",
            "abcd",
            true
        )
    }

}