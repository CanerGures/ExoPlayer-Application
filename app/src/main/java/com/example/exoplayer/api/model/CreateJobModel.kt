package com.example.exoplayer.api.model

import com.google.gson.annotations.SerializedName

data class BaseModelPost(

    @SerializedName("Action") val action: String,
    @SerializedName("APIKey") val aPIKey: String,
    @SerializedName("Job") val job: Job
)

data class Job(

    @SerializedName("Name") val name: String,
    @SerializedName("Inputs") val inputs: List<Inputs>,
    @SerializedName("Outputs") val outputs: List<Outputs>
)

data class Inputs(

    @SerializedName("Address") val address: String,
    @SerializedName("Streams") val streams: List<Streams>
)

data class Outputs(

    @SerializedName("Address") val address: String,
    @SerializedName("HLS") val hLS: HLS,
    @SerializedName("Streams") val streams: List<Streams>
)

data class Streams(

    @SerializedName("StreamType") val streamType: String,
    @SerializedName("StreamPID") val streamPID: Int,
    @SerializedName("StreamName") val streamName: String,
    @SerializedName("BitRate") val bitRate: Int,
    @SerializedName("Width") val width: Int,
    @SerializedName("Height") val height: Int,
    @SerializedName("VideoCodec") val videoCodec: String,
    @SerializedName("VideoProfileLevel") val videoProfileLevel: Double,
    @SerializedName("VideoTargetUsage") val videoTargetUsage: Int,
    @SerializedName("VideoGOPLength") val videoGOPLength: Int,
    @SerializedName("VideoRCMode") val videoRCMode: String,
    @SerializedName("VideoEntropyCodingMode") val videoEntropyCodingMode: String,
    @SerializedName("VideoIntraRefreshEnableFlag") val videoIntraRefreshEnableFlag: Boolean,
    @SerializedName("VideoRepeatSPSPPS") val videoRepeatSPSPPS: Boolean,
    @SerializedName("VideoDisableSPSPPS") val videoDisableSPSPPS: Boolean
)

data class HLS(

    @SerializedName("SegmentDelete") val segmentDelete: Boolean,
    @SerializedName("AllowCache") val allowCache: Boolean,
    @SerializedName("IsSingleFile") val isSingleFile: Boolean,
    @SerializedName("AudioOnly") val audioOnly: Boolean,
    @SerializedName("NoEndList") val noEndList: Boolean,
    @SerializedName("RoundDurations") val roundDurations: Boolean,
    @SerializedName("DiscontStart") val discontStart: Boolean
)
