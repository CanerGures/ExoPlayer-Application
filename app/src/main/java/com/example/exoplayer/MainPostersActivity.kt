package com.example.exoplayer

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.exoplayer.adapter.MainItemsRecyclerAdapter
import com.example.exoplayer.api.apiservice.ApiService
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
    private val service: ApiService by lazy { WebClient.buildService(ApiService::class.java) }
    lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_posters)
        button = findViewById(R.id.postButton)

        val jobFilter = JobFilter(
                "2014-01-01T00:00:00",
                1
        )

        val postJobList = GetJobListModel(
                "GetJobList",
                "f986f2fbac4c6565a40e3e2310bd0603",
                jobFilter
        )

        val rep = GetJobListRepository(service, postJobList)
        viewModel = GetJobListViewModelFactory(rep).create(GetJobListViewModel::class.java)


        recycView = findViewById(R.id.recycItems)
        recycView.adapter = MainItemsRecyclerAdapter(list)
        recycView.layoutManager = LinearLayoutManager(this)

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

        val rep = JobRepository(service, bodyModel)
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