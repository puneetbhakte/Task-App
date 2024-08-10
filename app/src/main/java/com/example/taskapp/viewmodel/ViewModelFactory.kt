package com.example.taskapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.taskapp.repository.Repository

class ViewModelFactory(private val repository: Repository):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return com.example.taskapp.viewmodel.ViewModel(repository) as T
    }

}