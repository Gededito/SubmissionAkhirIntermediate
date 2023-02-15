package com.example.submissionakhirintermediate

import com.example.submissionakhirintermediate.data.model.LoginResponse
import com.example.submissionakhirintermediate.data.model.LoginResult
import com.example.submissionakhirintermediate.data.model.RegisterResponse

object AuthenticationDummy {

    fun provideLoginResponse(): LoginResponse = LoginResponse(LoginResult("", "IDK412", "ABCD"), false, "ok")

    fun provideRegisterResponse(): RegisterResponse = RegisterResponse(false, "Ok")

}