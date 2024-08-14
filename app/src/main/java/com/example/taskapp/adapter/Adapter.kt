package com.example.taskapp.adapter

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.DetailTask
import com.example.taskapp.R
import com.example.taskapp.databinding.SingleViewBinding
import com.example.taskapp.model.Task

class Adapter(
    private val context: Context,
    private val data: List<Task>,
):RecyclerView.Adapter<Adapter.TaskViewHolder>() {

    private var filteredData: List<Task> = data

    inner class TaskViewHolder(val binding:SingleViewBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = SingleViewBinding.inflate(LayoutInflater.from(context),parent,false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return filteredData.size
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentList = data[position]
        val drawable: Drawable?
        when(currentList.category){
            "University" ->  drawable = ContextCompat.getDrawable(context, R.drawable.mortarboard_1)
            "Grocery" ->  drawable = ContextCompat.getDrawable(context, R.drawable.bread_1)
            "Work" ->  drawable = ContextCompat.getDrawable(context, R.drawable.briefcase_1)
            "Sport" ->  drawable = ContextCompat.getDrawable(context, R.drawable.sport_1)
            "Design" ->  drawable = ContextCompat.getDrawable(context, R.drawable.design__1__1)
            "Social" ->  drawable = ContextCompat.getDrawable(context, R.drawable.megaphone_1)
            "Music" ->  drawable = ContextCompat.getDrawable(context, R.drawable.music)
            "Health" ->  drawable = ContextCompat.getDrawable(context, R.drawable.vector)
            "Movie" ->  drawable = ContextCompat.getDrawable(context, R.drawable.video_camera_1)
            "Home" ->  drawable = ContextCompat.getDrawable(context, R.drawable.home__2__1)
            else -> drawable = null
        }

        drawable?.setBounds(0, 0, 60, 60)
        holder.binding.categoryTv.setCompoundDrawables(drawable, null, null, null)
        holder.binding.categoryTv.setCompoundDrawablePadding(10)
        holder.binding.titleTv.text = currentList.title
        if (currentList.minute.isNullOrBlank() || currentList.hour.isNullOrBlank()){
            holder.binding.description.text = "DeadLine : Not Defined"
        }else{
            holder.binding.description.text =  "Date -${currentList.date} \n Time - ${currentList.hour}:${currentList.minute}"
        }


        if (currentList.category.isNullOrBlank()){
            holder.binding.categoryTv.isVisible = false
        }else{
            holder.binding.categoryTv.text = currentList.category
        }
        if (currentList.priority.isNullOrBlank()){
            holder.binding.priorityTv.text = "Default"
        }else{
            holder.binding.priorityTv.text = currentList.priority
        }
      holder.itemView.setOnClickListener {
          val intent = Intent(context,DetailTask::class.java)
          intent.putExtra("id",currentList.id)
          context.startActivity(intent)
      }

    }

    fun filter(query: String) {
        filteredData = if (query.isEmpty()) {
            data
        } else {
            data.filter {
                it.title.contains(query, ignoreCase =true )
            }

        }
        notifyDataSetChanged()
    }

}