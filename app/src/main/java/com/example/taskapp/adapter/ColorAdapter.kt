package com.example.taskapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.databinding.ColorSingleViewBinding
import com.example.taskapp.databinding.IconSingleViewBinding

class ColorAdapter(private val context: Context, private val data:List<Int>, private val itemClickListener: ItemClickListener): RecyclerView.Adapter<ColorAdapter.ColorViewHolder>()  {

    inner class ColorViewHolder( val binding: ColorSingleViewBinding):RecyclerView.ViewHolder(binding.root)
    private var selectedPosition: Int = RecyclerView.NO_POSITION


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {

        val binding  = ColorSingleViewBinding.inflate(LayoutInflater.from(context),parent,false)
        return ColorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val currentList = data[position]
        holder.binding.color.setCardBackgroundColor(ContextCompat.getColor(context, currentList))
        if (position == selectedPosition) {
            holder.binding.ivColor.visibility = View.VISIBLE
        } else {
            holder.binding.ivColor.visibility = View.INVISIBLE

        }

        holder.itemView.setOnClickListener {
            // Update selected position and notify the adapter
            val previousPosition = selectedPosition
            selectedPosition = if (selectedPosition == position) {
                RecyclerView.NO_POSITION
            } else {
                position
            }
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            itemClickListener.onItemClick(position)
        }
        }



    override fun getItemCount(): Int {
        return data.size
    }
}
interface ItemClickListener {
    fun onItemClick(position: Int)
}