package com.example.yesican.di

import android.app.Application
import androidx.room.Room
import com.example.yesican.data.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.coroutines.coroutineContext

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app:Application,
        callback: TaskDatabase.Callback
    ) =
        Room.databaseBuilder(
            app,
            TaskDatabase::class.java,
            "tasksdb"
        )
            .fallbackToDestructiveMigration()
            //this call back is not executed at the time of database initialization but
                //a little bit latter after the build method finished
            .addCallback(callback)
            .build()

    @Provides//this is a singleton automatically
    fun provideTaskDao(db: TaskDatabase) = db.taskDao

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())

}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope