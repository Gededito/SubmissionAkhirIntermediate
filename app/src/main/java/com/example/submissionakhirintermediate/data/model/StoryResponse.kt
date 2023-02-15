package com.example.submissionakhirintermediate.data.model

import com.google.gson.annotations.SerializedName

data class StoryResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("listStory")
    val listStory: List<StoryApp>
)
