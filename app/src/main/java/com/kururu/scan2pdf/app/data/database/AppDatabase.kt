package com.kururu.scan2pdf.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kururu.scan2pdf.app.data.dao.FileDao
import com.kururu.scan2pdf.app.data.model.FileDetails
//import com.kururu.scan2pdf.app.utils.LocalDateTimeConverter


@Database(entities = arrayOf(FileDetails::class), version = 3, exportSchema = false)
//@TypeConverters(LocalDateTimeConverter::class)
public abstract class AppDatabase : RoomDatabase() {
    abstract fun fileDao(): FileDao
//
//    companion object {
//        // Singleton prevents multiple instances of database opening at the
//        // same time.
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getDatabase(context: Context): AppDatabase {
//            // if the INSTANCE is not null, then return it,
//            // if it is, then create the database
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "appDatabase"
//                ).build()
//                INSTANCE = instance
//                // return instance
//                instance
//            }
//        }
   // }

}