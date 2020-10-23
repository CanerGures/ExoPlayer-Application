package com.example.exoplayer.api.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.exoplayer.api.repo.JobRepository

class ViewModelFactory(private val repository: JobRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(CreateJobViewModel::class.java) -> {
                CreateJobViewModel(repository) as T
            }
            else -> {
                throw IllegalArgumentException("Not Assignable Class")
            }
        }
    }
}
