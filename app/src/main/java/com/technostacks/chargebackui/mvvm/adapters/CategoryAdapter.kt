package com.technostacks.chargebackui.mvvm.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.technostacks.chargebackui.databinding.ItemCategoryBinding
import com.technostacks.chargebackui.data.Category

class CategoryAdapter(
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var categories: List<Category> = emptyList()
    private var selectedCategory: Category? = null

    fun submitList(list: List<Category>) {
        categories = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount() = categories.size

    inner class ViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.ivCategoryIcon.setImageResource(category.iconRes)
            binding.tvCategoryName.text = category.name

            if (selectedCategory == category) {
                binding.ivCheckIcon.visibility = View.VISIBLE
            } else {
                binding.ivCheckIcon.visibility = View.GONE
            }

            itemView.setOnClickListener {
                if (selectedCategory == category) {
                    selectedCategory = null
                } else {
                    selectedCategory = category
                }

                notifyDataSetChanged()

                onCategoryClick(category)
            }
        }
    }
}
