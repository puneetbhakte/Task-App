package com.example.taskapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Category")
data class Category(
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0,
    @ColumnInfo
    val name:String,
    @ColumnInfo
    val color:Int,
    @ColumnInfo
    val icon:Int
)
