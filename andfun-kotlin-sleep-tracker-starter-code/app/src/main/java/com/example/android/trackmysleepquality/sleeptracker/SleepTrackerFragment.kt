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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.databinding.FragmentSleepTrackerBinding
import com.google.android.material.snackbar.Snackbar

class SleepTrackerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepTrackerBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_tracker, container, false)

        val application = requireNotNull(this.activity).application
        val sleepDatabaseDao = SleepDatabase.getInstance(application).dao

        val trackerViewModelFactory = SleepTrackerViewModelFactory(sleepDatabaseDao, application)
        val viewModel = ViewModelProvider(this, trackerViewModelFactory).get(SleepTrackerViewModel::class.java)

        val layoutManager = GridLayoutManager(activity, 3)

        val adapter = SleepNightAdapter(SleepNightListener {
            viewModel.onSleepNightClicked(it.id)
        })


        binding.recyclerList.layoutManager = layoutManager
        binding.recyclerList.adapter = adapter

        binding.lifecycleOwner = this
        binding.trackerViewModel = viewModel

        viewModel.navigateToSleepDataQuality.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController().navigate(SleepTrackerFragmentDirections.actionSleepTrackerFragmentToSleepQualityFragment(it))
                viewModel.onSleepQualityNavigated()
            }
        })

        viewModel.allNights.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.navigateToSleepQuality.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController()
                        .navigate(SleepTrackerFragmentDirections
                                .actionSleepTrackerFragmentToSleepQualityFragment(it.id))
                viewModel.doneNavigating()
            }
        })

        viewModel.alertEvent.observe(viewLifecycleOwner, Observer {
            if (it) {
                Snackbar.make(activity!!.findViewById(android.R.id.content),
                        getString(R.string.cleared_message)
                        , Snackbar.LENGTH_SHORT).show()
            }
            viewModel.doneNavigating()
        })

        return binding.root
    }
}
