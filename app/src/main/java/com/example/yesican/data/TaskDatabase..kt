package com.example.yesican.data;

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.yesican.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract val taskDao: TaskDao

    class Callback @Inject constructor(
    //using provider is a way to get the database into the callback and instantiated it later
    //if use just an instance of database this would be a circular dependency so because database instance needs a callback
    // and a callback uses an instance of a database
        //with a Provider we can get depenencies lazily
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) :RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = database.get().taskDao

            applicationScope.launch {
                dao.insert(Task("Wash the dishes"))
                dao.insert(Task("Do the laundry"))
                dao.insert(Task("Buy Groceries",important = true))
                dao.insert(Task("Prepare food",completed = true))
                dao.insert(Task("call mom"))
                dao.insert(Task("visit grandma",completed = true))
                dao.insert(Task("Call Elon musk"))
            }
        }
    }
}