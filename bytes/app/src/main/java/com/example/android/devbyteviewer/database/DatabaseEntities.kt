package com.example.android.devbyteviewer.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.devbyteviewer.domain.Video

@Entity(tableName = "databaseVideoTable")
data class DatabaseVideo(
        @PrimaryKey
        val url: String,
        val updated: String,
        val title: String,
        val description: String,
        val thumbnail: String
)

fun List<DatabaseVideo>.asDomainModel(): List<Video> {
    return map {
        Video(
                url = it.url,
                title = it.title,
                description = it.description,
                thumbnail = it.thumbnail,
                updated = it.updated

        )
    }
}