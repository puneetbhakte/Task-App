package com.example.taskapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.taskapp.model.Task

@Dao
interface TaskDao {

    @Upsert
    suspend fun insertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM Task ORDER BY id DESC")
     fun getData(): LiveData<List<Task>>

    @Query("SELECT * FROM Task WHERE id = :taskId")
     fun getTaskById(taskId: Int): LiveData<Task>

    @Query("SELECT * FROM Task WHERE title LIKE :searchQuery ORDER BY id DESC")
    fun searchTasksByTitle(searchQuery: String): LiveData<List<Task>>
}