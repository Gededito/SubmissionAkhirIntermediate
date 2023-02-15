package com.example.submissionakhirintermediate.data.model

import com.google.gson.annotations.SerializedName

data class AddStoryResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("messsage")
    val message: String? = null
)
