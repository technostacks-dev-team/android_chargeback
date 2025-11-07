package com.technostacks.chargebackui.mvvm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technostacks.chargebackui.databinding.ItemFrequencyBinding
import com.technostacks.chargebackui.data.Frequency

class FrequencyAdapter(
    private val onFrequencyClick: (Frequency) -> Unit
) : RecyclerView.Adapter<FrequencyAdapter.ViewHolder>() {

    private var frequencies: List<Frequency> = emptyList()
    private var selectedFrequency: Frequency? = null

    fun submitList(list: List<Frequency>) {
        frequencies = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFrequencyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(frequencies[position])
    }

    override fun getItemCount() = frequencies.size

    inner class ViewHolder(private val binding: ItemFrequencyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(frequency: Frequency) {
            binding.tvFrequencyName.text = frequency.name

            // Check if this item is the selected one
            binding.ivCheckIcon.visibility = if (frequency.id == selectedFrequency?.id) {
                View.VISIBLE
            } else {
                View.GONE
            }

            itemView.setOnClickListener {
                if (selectedFrequency?.id != frequency.id) {
                    val previousSelectedPosition = frequencies.indexOf(selectedFrequency)
                    selectedFrequency = frequency

                    // Notify the previously selected item to hide checkmark
                    if (previousSelectedPosition != -1) {
                        notifyItemChanged(previousSelectedPosition)
                    }
                    // Notify the newly selected item to show checkmark
                    notifyItemChanged(adapterPosition)
                }
                onFrequencyClick(frequency)
            }
        }
    }
}
