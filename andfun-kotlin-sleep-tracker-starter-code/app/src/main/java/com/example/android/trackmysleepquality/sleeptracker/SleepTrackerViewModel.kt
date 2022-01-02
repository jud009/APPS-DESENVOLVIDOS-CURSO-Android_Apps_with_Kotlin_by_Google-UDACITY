/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.formatNights
import kotlinx.coroutines.*

/**
 * ViewModel for SleepTrackerFragment.
 */
class SleepTrackerViewModel(
        val database: SleepDatabaseDao,
        application: Application) : AndroidViewModel(application) {

    private val coroutineJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + coroutineJob)

    private var tonight = MutableLiveData<SleepNight?>()

    val allNights = database.getAllNights()

    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()
    val navigateToSleepQuality: LiveData<SleepNight> get() = _navigateToSleepQuality

    private val _alertEvent = MutableLiveData<Boolean>()
    val alertEvent: LiveData<Boolean> get() = _alertEvent

    private val _navigateToSleepDataQuality = MutableLiveData<Long>()
    val navigateToSleepDataQuality: LiveData<Long> get() = _navigateToSleepDataQuality

    val startButtonVisible = Transformations.map(tonight) {
        it == null
    }

    val stopButtonVisible = Transformations.map(tonight) {
        null != it
    }

    val clearButtonVisible = Transformations.map(allNights) {
        it?.isNotEmpty()
    }

    val latestNights = Transformations.map(allNights) {
        formatNights(it, application.resources)
    }


    init {
        getToNightFromDb()
    }

    fun onSleepNightClicked(id: Long){
        _navigateToSleepDataQuality.value = id
    }

    fun onSleepQualityNavigated(){
        _navigateToSleepDataQuality.value = null
    }

    fun doneShowingSnackBar() {
        _alertEvent.value = false
    }

    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }


    private fun getToNightFromDb() {
        uiScope.launch {
            tonight.value = getToNight()
        }
    }


    fun startTracking() {
        uiScope.launch {
            val sleep = SleepNight()

            insert(sleep)

            tonight.value = getToNight()
        }
    }

    fun stopTracking() {
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            _navigateToSleepQuality.value = oldNight
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
            tonight.value = null
            _alertEvent.value = true
        }
    }


    private suspend fun insert(sleep: SleepNight) {
        withContext(Dispatchers.IO) {
            database.insert(sleep)
        }
    }

    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.update(night)
        }
    }

    private suspend fun getToNight(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night: SleepNight? = database.getTonight()
            if (night?.endTimeMilli != night?.startTimeMilli) {
                night = null
            }
            return@withContext night
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }


    override fun onCleared() {
        super.onCleared()
        coroutineJob.cancel()
    }

}

