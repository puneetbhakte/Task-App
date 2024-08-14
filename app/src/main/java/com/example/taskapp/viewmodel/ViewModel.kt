package com.example.taskapp.viewmodel

import android.icu.text.CaseMap.Title
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.model.Category
import com.example.taskapp.model.Task
import com.example.taskapp.repository.Repository
import kotlinx.coroutines.launch

class ViewModel(private val repository: Repository):ViewModel() {


    fun upsertTask(task: Task){
        viewModelScope.launch {
            repository.insertData(task)
        }
    }
    fun deleteTask(task: Task){
        viewModelScope.launch {
            repository.deleteData(task)
        }
    }
    fun searchTitle(title: String) = viewModelScope.launch {repository.searchByTitle(title) }

    fun insertCategory(category: Category){
        viewModelScope.launch {
            repository.insertCategory(category)
        }
    }
    fun deleteCategory(category: Category){
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }

    fun getCategory() = repository.getCategory()
    fun getTask() = repository.getAllData()
    fun getTaskById(id:Int) = repository.getDataById(id)



}