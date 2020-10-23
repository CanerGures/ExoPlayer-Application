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
import com.example.exoplayer.api.repo.JobRepository
import com.example.exoplayer.api.viewmodel.CreateJobViewModel
import com.example.exoplayer.api.viewmodel.ViewModelFactory
import com.example.exoplayer.model.MainDashboardModel

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

        val list = ArrayList<MainDashboardModel>()

        val mainCard1 = MainDashboardModel(
                "https://dash.akamaized.net/dash264/TestCases/5a/nomor/1.mpd",
                1,
                "https://m.media-amazon.com/images/M/MV5BNzA5ZDNlZWMtM2NhNS00NDJjLTk4NDItYTRmY2EwMWZlMTY3XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_.jpg",
                "Test1"
        )
        val mainCard2 = MainDashboardModel(
                "https://dash.akamaized.net/akamai/bbb_30fps/bbb_30fps.mpd",
                2,
                "https://m.media-amazon.com/images/M/MV5BMTMxNTMwODM0NF5BMl5BanBnXkFtZTcwODAyMTk2Mw@@._V1_.jpg",
                "Test1"
        )
        val mainCard3 = MainDashboardModel(
                "https://dash.akamaized.net/dash264/TestCases/5a/nomor/1.mpd",
                3,
                "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_.jpg",
                "Test1"
        )
        val mainCard4 = MainDashboardModel(
                "https://dash.akamaized.net/akamai/bbb_30fps/bbb_30fps.mpd",
                4,
                "https://m.media-amazon.com/images/M/MV5BYzg0NGM2NjAtNmIxOC00MDJmLTg5ZmYtYzM0MTE4NWE2NzlhXkEyXkFqcGdeQXVyMTA4NjE0NjEy._V1_.jpg",
                "Test1"
        )

        list.add(mainCard1)
        list.add(mainCard2)
        list.add(mainCard3)
        list.add(mainCard4)

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
                "cf05a774566f3e24a86ad090d12e474f",
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