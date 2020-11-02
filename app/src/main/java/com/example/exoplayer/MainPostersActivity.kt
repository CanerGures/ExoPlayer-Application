package com.example.exoplayer

import android.os.Bundle
import android.os.StrictMode
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exoplayer.adapter.GetJobListAdapter
import com.example.exoplayer.api.apiservice.ApiService
import com.example.exoplayer.api.apiservice.GetJobApiService
import com.example.exoplayer.api.client.WebClient
import com.example.exoplayer.api.model.GetJobListModel
import com.example.exoplayer.api.model.GetJobsList
import com.example.exoplayer.api.model.JobFilter
import com.example.exoplayer.api.repo.GetJobListRepository
import com.example.exoplayer.api.viewmodel.CreateJobViewModel
import com.example.exoplayer.api.viewmodel.GetJobListViewModel
import com.example.exoplayer.api.viewmodel.GetJobListViewModelFactory


class MainPostersActivity : AppCompatActivity() {
    lateinit var recycView: RecyclerView
    private var viewModel: CreateJobViewModel? = null
    private var getJobViewModel: GetJobListViewModel? = null
    private val service: GetJobApiService by lazy { WebClient.buildService(GetJobApiService::class.java) }
    private val serviceCreate: ApiService by lazy { WebClient.buildService(ApiService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_posters)
        supportActionBar?.hide()
        val jobFilter = JobFilter(
            "2014-01-01T00:00:00",
            2
        )

        val postJobList = GetJobListModel(
            "GetJobList",
            "38252360f94f35c373023a88112f3ae3",
            jobFilter
        )

        val rep = GetJobListRepository(service, postJobList)
        getJobViewModel = GetJobListViewModelFactory(rep).create(GetJobListViewModel::class.java)
        getJobViewModel?.fetchLive?.observe(this){
            val list : List<GetJobsList> = (it.jobs)
            recycView = findViewById(R.id.recycItems)
            recycView.adapter = GetJobListAdapter(list)
            recycView.layoutManager = LinearLayoutManager(this)

        }

    }

    private fun observePost() {
        viewModel?.fetchLive?.observe(this) {
            if (it.stateCode == "SUCCESS")
                Toast.makeText(this, "Api Successful!", Toast.LENGTH_LONG).show()
            else (
                    Toast.makeText(this, "Api Is Not Working!", Toast.LENGTH_LONG).show()
                    )
        }
    }
}