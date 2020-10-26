package com.example.exoplayer.api.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GetJobListResponseModel(

        @SerializedName("Action") val action: String,
        @SerializedName("StateCode") val stateCode: String,
        @SerializedName("State") val state: String,
        @SerializedName("Jobs") val jobs: List<GetJobsList>,
        @SerializedName("JobStatistics") val jobStatistics: JobStatistics
) : Serializable

data class GetJobsList(

        @SerializedName("ID") val iD: Int,
        @SerializedName("State") val state: String,
        @SerializedName("Message") val message: String,
        @SerializedName("UserID") val userID: Int,
        @SerializedName("Progress") val progress: Int,
        @SerializedName("Name") val name: String,
        @SerializedName("StreamLink") val streamLink: String,
        @SerializedName("Region") val region: String,
        @SerializedName("CreateDate") val createDate: String,
        @SerializedName("CompletedDate") val completedDate: String,
        @SerializedName("ContentDuration") val contentDuration: Double,
        @SerializedName("InQueueDuration") val inQueueDuration: Int,
        @SerializedName("IngestDuration") val ingestDuration: Int
) : Serializable

data class JobStatistics(

        @SerializedName("Count") val count: Int,
        @SerializedName("JobStates") val jobStates: String,
        @SerializedName("duration") val duration: String,
        @SerializedName("Usage") val usage: String
) : Serializable