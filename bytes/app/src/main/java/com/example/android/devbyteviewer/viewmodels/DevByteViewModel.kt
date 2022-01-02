package com.example.android.devbyteviewer.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.devbyteviewer.database.getInstance
import com.example.android.devbyteviewer.repository.VideosRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DevByteViewModel(application: Application) : AndroidViewModel(application) {

    private val mJob = Job()
    private val coroutine = CoroutineScope(mJob + Dispatchers.Main)

    private val dataBase = getInstance(application)
    private val videosRepository = VideosRepository(dataBase)

    val playlist = videosRepository.videos

    init {
        coroutine.launch {
            videosRepository.refreshVideos()
        }
    }

    override fun onCleared() {
        super.onCleared()
        mJob.cancel()
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DevByteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DevByteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
