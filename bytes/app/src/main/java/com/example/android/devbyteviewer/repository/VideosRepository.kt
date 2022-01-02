package com.example.android.devbyteviewer.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.devbyteviewer.database.VideoDataBase
import com.example.android.devbyteviewer.database.asDomainModel
import com.example.android.devbyteviewer.domain.Video
import com.example.android.devbyteviewer.network.Network
import com.example.android.devbyteviewer.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class VideosRepository(private val dataBase: VideoDataBase) {

    val videos: LiveData<List<Video>> =
            Transformations.map(dataBase.videoDao.getVideos()) {
                it.asDomainModel()
            }

    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            val networkData = Network.devbytes.getPlaylist().await()
            dataBase.videoDao.insertAll(*networkData.asDatabaseModel())
        }
    }
}
