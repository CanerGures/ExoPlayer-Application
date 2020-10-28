package com.example.exoplayer

import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exoplayer.adapter.GetJobListAdapter
import com.example.exoplayer.api.apiservice.ApiService
import com.example.exoplayer.api.apiservice.GetJobApiService
import com.example.exoplayer.api.client.WebClient
import com.example.exoplayer.api.model.*
import com.example.exoplayer.api.repo.GetJobListRepository
import com.example.exoplayer.api.repo.JobRepository
import com.example.exoplayer.api.viewmodel.CreateJobViewModel
import com.example.exoplayer.api.viewmodel.GetJobListViewModel
import com.example.exoplayer.api.viewmodel.GetJobListViewModelFactory
import com.example.exoplayer.api.viewmodel.ViewModelFactory


class MainPostersActivity : AppCompatActivity() {
    lateinit var recycView: RecyclerView
    lateinit var button: Button

    private var viewModel: CreateJobViewModel? = null
    private var getJobViewModel: GetJobListViewModel? = null
    private val service: GetJobApiService by lazy { WebClient.buildService(GetJobApiService::class.java) }
    private val serviceCreate: ApiService by lazy { WebClient.buildService(ApiService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_posters)
        button = findViewById(R.id.postButton)
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



        button.setOnClickListener {
            createJob()
        }
    }

    private fun createJob() {

        val streams = Streams(
            "Video",
            1,
            "Video_250K",
            250000,
            416,
            234,
            "H264_BASELINE",
            3.0,
            4,
            100,
            "None",
            "CAVLC",
            false,
            false,
            false
        )

        val hls = HLS(
            false,
            false,
            false,
            false,
            false,
            false,
            false
        )
        val inputs = Inputs(
            "http://www.ngcloudvideo.com/sample/NgVideo.mp4",
            listOf(streams)
        )
        val outputs = Outputs(
            "videobulut://",
            hls,
            listOf(streams)
        )
        val job = Job(
            "CreateJobSample",
            listOf(inputs),
            listOf(outputs)
        )
        val bodyModel = BaseModelPost(
            "CreateJob",
            "f986f2fbac4c6565a40e3e2310bd0603",
            job
        )

        val rep = JobRepository(serviceCreate, bodyModel)
        viewModel = ViewModelFactory(rep).create(CreateJobViewModel::class.java)
        observePost()

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