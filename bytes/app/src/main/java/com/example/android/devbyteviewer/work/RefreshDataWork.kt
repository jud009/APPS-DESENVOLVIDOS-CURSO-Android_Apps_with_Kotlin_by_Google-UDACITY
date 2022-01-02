package com.example.android.devbyteviewer.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.devbyteviewer.database.getInstance
import com.example.android.devbyteviewer.repository.VideosRepository
import retrofit2.HttpException


class RefreshDataWork(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {

    companion object{
        const val WORK_NAME = "REFRESH_DATA_WORK"
    }


    override suspend fun doWork(): Result {
        val database = getInstance(applicationContext)
        val networkRepository = VideosRepository(database)

        return try {
            networkRepository.refreshVideos()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

}