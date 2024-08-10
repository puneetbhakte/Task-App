package com.example.taskapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Task")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo
    val title:String,
    @ColumnInfo
    val description:String,
    @ColumnInfo
    val date:String?,
    @ColumnInfo
    val category: String?,
    @ColumnInfo
    val priority:String?,
    @ColumnInfo
    val minute:String?,
    @ColumnInfo
    val hour:String?
    )


