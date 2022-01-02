package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.AdapterSleepNightBinding

class SleepNightAdapter(private val listener: SleepNightListener) : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()) {

    //listAdapter is faster and diffUtil will figure out the minimum changes to do in the adapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, listener)
        //é uma boa delegar lógica para viewHolder
    }

    class ViewHolder private constructor(val binding: AdapterSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SleepNight, listener: SleepNightListener) {
            binding.sleepNight = item
            binding.clickListener = listener
            binding.executePendingBindings() //otimização, boa pratica recomendada(faster)
        }

        companion object {
            fun from(group: ViewGroup): ViewHolder {
                val inflater: LayoutInflater = LayoutInflater.from(group.context)
                val binding = AdapterSleepNightBinding.inflate(inflater, group, false)
                return ViewHolder(binding)
            }
        }
    }

}

class SleepNightDiffCallback() : DiffUtil.ItemCallback<SleepNight>() {
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean = oldItem == newItem
}

class SleepNightListener(val clickListener: (night: SleepNight) -> Unit){
    fun onClick(night: SleepNight) = clickListener(night)
}