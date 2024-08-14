package com.example.taskapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.R
import com.example.taskapp.databinding.IconSingleViewBinding

class IconAdapter(private val context: Context, private val data:List<Int>,private val iconItemClickListener: IconItemClickListener):RecyclerView.Adapter<IconAdapter.IconViewHolder>() {

    inner class IconViewHolder( val binding:IconSingleViewBinding):RecyclerView.ViewHolder(binding.root)
    private var selectedPosition: Int = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {

        val binding  = IconSingleViewBinding.inflate(LayoutInflater.from(context),parent,false)
        return IconViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        val currentList = data[position]
         holder.binding.ivIcon.setImageResource(currentList)
        if (position == selectedPosition) {
            holder.binding.work.setBackgroundResource(R.drawable.border_bg)
        } else {
            holder.binding.work.setBackgroundResource(R.drawable.no_border)

        }
        holder.itemView.setOnClickListener {
            iconItemClickListener.onIconItemClick(position)
            val previousPosition = selectedPosition
            selectedPosition = if (selectedPosition == position) {
                RecyclerView.NO_POSITION
            } else {
                position
            }
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

}

interface IconItemClickListener {
    fun onIconItemClick(position: Int)
}