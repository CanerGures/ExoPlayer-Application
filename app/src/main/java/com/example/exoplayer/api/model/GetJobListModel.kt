package com.example.exoplayer.api.model

import com.google.gson.annotations.SerializedName

data class GetJobListModel(

        @SerializedName("Action") val action: String,
        @SerializedName("APIKey") val aPIKey: String,
        @SerializedName("JobFilter") val jobFilter: JobFilter
)

data class JobFilter(

        @SerializedName("FirstDate") val firstDate: String,
        @SerializedName("Page") val page: Int
)