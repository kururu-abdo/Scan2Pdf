package com.kururu.scan2pdf.app.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kururu.scan2pdf.app.data.model.FileDetails
import kotlinx.coroutines.flow.Flow


@Dao
interface FileDao {
    @Query("SELECT * FROM FileDetails ")
    fun getAllFiles(): Flow<List<FileDetails>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(file: FileDetails)

    @Query("DELETE FROM FileDetails Where fileId =:id")
    suspend fun delete(id:Int)

}