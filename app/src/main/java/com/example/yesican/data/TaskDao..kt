package com.example.yesican.data;

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task where name Like '%' || :searchQuery || '%' ORDER BY important DESC")
    fun getAllTasks(searchQuery: String): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: Task): Long

    @Update
    suspend fun update(task: Task)

    @Query("SELECT * from task WHERE Id = :key")
    suspend fun get(key: Long): Task?

    @Delete
    suspend fun delete(task: Task)

    @Query("DELETE FROM task")
    suspend fun clear()

}