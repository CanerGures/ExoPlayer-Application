package com.example.exoplayer.api.model

import com.google.gson.annotations.SerializedName

data class BaseModelResponse(

        @SerializedName("Action") val action: String,
        @SerializedName("StateCode") val stateCode: String,
        @SerializedName("State") val state: String,
        @SerializedName("Job") val job: JobResponse
)

data class JobResponse(

    @SerializedName("ID") val iD: Int,
    @SerializedName("State") val state: String,
    @SerializedName("UserID") val userID: Int,
    @SerializedName("Name") val name: String,
    @SerializedName("Region") val region: String,
    @SerializedName("CreateDate") val createDate: String,
    @SerializedName("StartDate") val startDate: String,
    @SerializedName("ContentDuration") val contentDuration: Int,
    @SerializedName("Inputs") val inputs: List<InputsResponse>,
    @SerializedName("Outputs") val outputs: List<OutputsResponse>
)

data class InputsResponse(

    @SerializedName("UID") val uID: String,
    @SerializedName("Address") val address: String,
    @SerializedName("ItemSize") val itemSize: Int,
    @SerializedName("ExpiryPolicy") val expiryPolicy: String,
    @SerializedName("Streams") val streams: List<StreamsResponse>
)

data class HLSResponse(

    @SerializedName("SegmentDelete") val segmentDelete: Boolean,
    @SerializedName("AllowCache") val allowCache: Boolean,
    @SerializedName("IsSingleFile") val isSingleFile: Boolean,
    @SerializedName("AudioOnly") val audioOnly: Boolean,
    @SerializedName("NoEndList") val noEndList: Boolean,
    @SerializedName("RoundDurations") val roundDurations: Boolean,
    @SerializedName("DiscontStart") val discontStart: Boolean
)

data class MetadataResponse(

    @SerializedName("Title") val title: String,
    @SerializedName("CopyRight") val copyRight: String,
    @SerializedName("Author") val author: String,
    @SerializedName("Description") val description: String,
    @SerializedName("Album") val album: String
)

data class OutputsResponse(

        @SerializedName("DeliveryState") val deliveryState: String,
        @SerializedName("DeliveryDuration") val deliveryDuration: Int,
        @SerializedName("HLS") val hLS: HLSResponse,
        @SerializedName("Streams") val streams: List<StreamsResponse>,
        @SerializedName("Metadata") val metadata: Metadata,
        @SerializedName("UID") val uID: String,
        @SerializedName("Address") val address: String
)

data class StreamsResponse(

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