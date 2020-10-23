package com.example.exoplayer.model

import java.io.Serializable

data class MainDashboardModel(
    val streamUrl: String,
    val id: Int,
    val posterUrl: String,
    val title: String
) : Serializable