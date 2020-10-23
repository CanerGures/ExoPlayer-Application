package com.example.exoplayer.api.repo

import com.example.exoplayer.api.apiservice.ApiService
import com.example.exoplayer.api.model.BaseModelPost

class JobRepository(
    private val service: ApiService,
    private val createJobBody: BaseModelPost
) {

    fun postJobRepo() = service.createJob(createJobBody)
}