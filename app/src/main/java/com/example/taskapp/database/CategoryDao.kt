package com.example.taskapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.taskapp.model.Category
import com.example.taskapp.model.Task

@Dao
interface CategoryDao {
    @Upsert
    suspend fun insertCategory(category: Category)

    @Delete
    suspend fun deleteTask(category: Category)


    @Query("SELECT * FROM Category ORDER BY id DESC")
    fun getCategory():LiveData<List<Category>>

}