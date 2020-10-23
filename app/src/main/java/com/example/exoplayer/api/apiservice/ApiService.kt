package com.example.exoplayer.api.apiservice

import com.example.exoplayer.api.model.BaseModelPost
import com.example.exoplayer.api.model.BaseModelResponse

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @Headers(
        "Content-type:application/json"
    )
    @POST("/Cloud")
    fun createJob(@Body cJob: BaseModelPost): Call<BaseModelResponse>
}