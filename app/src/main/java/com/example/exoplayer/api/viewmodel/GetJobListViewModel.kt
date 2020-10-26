package com.example.exoplayer.api.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.exoplayer.api.model.GetJobListResponseModel
import com.example.exoplayer.api.repo.GetJobListRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetJobListViewModel(private val jobRepository: GetJobListRepository) :
        BaseViewModel<GetJobListResponseModel>() {

    val fetch = MutableLiveData<GetJobListResponseModel>()
    val fetchLive: LiveData<GetJobListResponseModel> = fetch

    init {
        createJobApi()
    }

    private fun createJobApi() {
        dataLoading.value = true

        jobRepository.getJobListRepo().enqueue(object : Callback<GetJobListResponseModel> {
            override fun onResponse(
                    call: Call<GetJobListResponseModel>,
                    response: Response<GetJobListResponseModel>
            ) {
                if (!response.isSuccessful) {
                    empty.value = true
                    return
                }
                fetch.value = response.body()
                empty.value = false
            }

            override fun onFailure(call: Call<GetJobListResponseModel>?, t: Throwable?) {

            }
        })


    }

}