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
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MapsApiFilter
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
import com.example.android.marsrealestate.overview.MarsApiStatus.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

enum class MarsApiStatus { DONE, LOADING, ERROR }

class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the status of the most recent request
    private val _status = MutableLiveData<MarsApiStatus>()
    val status: LiveData<MarsApiStatus>
        get() = _status

    private val _property = MutableLiveData<List<MarsProperty>>()
    val property: LiveData<List<MarsProperty>>
        get() = _property

    private val navToDetailFragment = MutableLiveData<MarsProperty>()
    val getNavToDetailFragmentLiveData: LiveData<MarsProperty> get() = navToDetailFragment

    //coroutine
    private val mJob = Job()
    private val mCoroutine = CoroutineScope(mJob + Dispatchers.Main)

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getMarsRealEstateProperties(MapsApiFilter.SHOW_ALL)
    }

    /**
     * Sets the value of the status LiveData to the Mars API status.
     */
    private fun getMarsRealEstateProperties(filter: MapsApiFilter) {
        _status.value = LOADING

        mCoroutine.launch {
            try {
                val result = MarsApi.retrofitService.getProperties(filter.value)
                if (result.isNotEmpty()) {
                    _property.value = result
                    _status.value = DONE
                }
            } catch (e: Exception) {
                _status.value = ERROR
                _property.value = emptyList()
            }
        }
    }

    fun updateFilter(filter: MapsApiFilter) {
        getMarsRealEstateProperties(filter)
    }

    fun goToDetailFragment(marsProperty: MarsProperty) {
        navToDetailFragment.value = marsProperty
    }

    fun goToDetailFragmentCompleted() {
        navToDetailFragment.value = null
    }

    override fun onCleared() {
        super.onCleared()
        mJob.cancel()
    }
}
