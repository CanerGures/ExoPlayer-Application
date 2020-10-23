package com.example.exoplayer.api.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.exoplayer.api.model.BaseModelResponse
import com.example.exoplayer.api.repo.JobRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateJobViewModel(private val jobRepository: JobRepository) :
    BaseViewModel<BaseModelResponse>() {

    val fetch = MutableLiveData<BaseModelResponse>()
    val fetchLive: LiveData<BaseModelResponse> = fetch

    init {
        createJobApi()
    }

    private fun createJobApi() {
        dataLoading.value = true

        jobRepository.postJobRepo().enqueue(object : Callback<BaseModelResponse> {
            override fun onResponse(
                call: Call<BaseModelResponse>,
                response: Response<BaseModelResponse>
            ) {
                if (!response.isSuccessful) {
                    empty.value = true
                    return
                }
                fetch.value = response.body()
                empty.value = false
            }

            override fun onFailure(call: Call<BaseModelResponse>?, t: Throwable?) {

            }
        })


    }

}