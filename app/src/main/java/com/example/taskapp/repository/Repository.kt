package com.example.taskapp.repository

import com.example.taskapp.database.Database
import com.example.taskapp.model.Task

class Repository(private val database: Database) {

    suspend fun insertData(task: Task){
        database.getTaskDao().insertTask(task)
    }

    suspend fun deleteData(task: Task){
        database.getTaskDao().deleteTask(task)
    }

    suspend fun searchByTitle(title:String){
        database.getTaskDao().searchTasksByTitle(title)
    }

     fun getAllData() = database.getTaskDao().getData()
     fun getDataById(id:Int) = database.getTaskDao().getTaskById(id)




}