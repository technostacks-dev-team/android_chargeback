package com.technostacks.chargebackui.mvvm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technostacks.chargebackui.R
import com.technostacks.chargebackui.databinding.ItemServiceBinding
import com.technostacks.chargebackui.data.Service

class ServicesAdapter(
    private val onServiceClick: (Service) -> Unit,
) : RecyclerView.Adapter<ServicesAdapter.ViewHolder>() {

    private var services: List<Service> = emptyList()
    private var selectedService: Service? = null

    fun submitList(list: List<Service>) {
        services = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(services[position])
    }

    override fun getItemCount() = services.size

    inner class ViewHolder(private val binding: ItemServiceBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(service: Service) {
            binding.ivServiceIcon.setImageResource(service.iconRes)
            binding.tvServiceName.text = service.name

            if (selectedService == service) {
                binding.ivCheckIcon.visibility = View.VISIBLE
            } else {
                binding.ivCheckIcon.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                if (selectedService == service) {
                    selectedService = null
                } else {
                    selectedService = service
                }

                notifyDataSetChanged()

                onServiceClick(service)
            }
        }
    }
}
