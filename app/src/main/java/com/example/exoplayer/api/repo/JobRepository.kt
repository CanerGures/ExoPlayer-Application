package com.example.exoplayer.api.repo

import com.example.exoplayer.api.apiservice.ApiService
import com.example.exoplayer.api.model.BaseModelPost
import com.example.exoplayer.api.model.GetJobListModel

class JobRepository(
        private val service: ApiService,
        private val createJobBody: BaseModelPost

) {

    fun postJobRepo() = service.createJob(createJobBody)

}

class GetJobListRepository(
        private val service: ApiService,
        private val getJobBody: GetJobListModel
) {
    fun getJobListRepo() = service.getJobList(getJobBody)
}