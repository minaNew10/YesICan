package com.example.yesican.data;

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.yesican.ui.tasks.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    fun getTasks(query: String, sortOrder: SortOrder,hideCompleted: Boolean): Flow<List<Task>> =
        when(sortOrder){
            SortOrder.BY_DATE -> getTasksSortedByDateCreated(query,hideCompleted)
            SortOrder.BY_NAME -> getTasksSortedByName(query,hideCompleted)
        }
    //you can't pass a column name dynamically
    @Query("SELECT * FROM task where (completed != :hideCompleted OR completed = 0) And name Like '%' || :searchQuery || '%' ORDER BY important DESC,name")
    fun getTasksSortedByName(searchQuery: String,hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task where (completed != :hideCompleted OR completed = 0) And name Like '%' || :searchQuery || '%' ORDER BY important DESC,created")
    fun getTasksSortedByDateCreated(searchQuery: String,hideCompleted: Boolean): Flow<List<Task>>

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