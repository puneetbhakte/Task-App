package com.example.taskapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.taskapp.model.Task

@Database(entities = [Task::class] , version = 4, exportSchema = true)
abstract class Database:RoomDatabase() {

    abstract fun getTaskDao():TaskDao

    companion object{
        @Volatile
        private var instance: com.example.taskapp.database.Database? = null
        private val LOCK = Any()


        operator fun invoke(context: Context) = instance ?:synchronized(LOCK)
        {
            instance ?:

            createdatabase(context).also {
                instance = it
            }

        }

        private fun createdatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, com.example.taskapp.database.Database::class.java,"my_Database")
                .fallbackToDestructiveMigration()
                .build()

    }

}
