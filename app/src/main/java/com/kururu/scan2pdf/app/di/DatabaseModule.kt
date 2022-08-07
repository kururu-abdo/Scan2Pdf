package com.kururu.scan2pdf.app.di

import android.content.Context
import androidx.room.Room
import com.kururu.scan2pdf.app.data.dao.FileDao
import com.kururu.scan2pdf.app.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideLogDao(database: AppDatabase): FileDao {
        return database.fileDao()
    }
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {




        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "appdatabase.db"
        ).build()
    }

}