package com.example.taskapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.MainActivity
import com.example.taskapp.databinding.CategorySingleViewBinding
import com.example.taskapp.model.Category

class CategoryAdapter(private var context: Context, private val data:List<Category>, private val categoryItemClickListener: MainActivity):RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(var binding:CategorySingleViewBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = CategorySingleViewBinding.inflate(LayoutInflater.from(context),parent,false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentList = data[position]
        holder.binding.grocery.setCardBackgroundColor(ContextCompat.getColor(context, currentList.color))
        holder.binding.groceryTv.text = currentList.name
        currentList.icon?.let { holder.binding.flag1.setImageResource(it) }
        holder.itemView.setOnClickListener {
            categoryItemClickListener.onCategoryClick(position)
        }
    }

    override fun getItemCount(): Int {
       return data.size
    }

}
interface CategoryItemClickListener {
    fun onCategoryClick(position: Int)
}