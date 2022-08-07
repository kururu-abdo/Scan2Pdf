package com.kururu.scan2pdf.app.data.repositories

import androidx.annotation.WorkerThread
import com.kururu.scan2pdf.app.data.dao.FileDao
import com.kururu.scan2pdf.app.data.model.FileDetails
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppRepository @Inject constructor (private val fileDao: FileDao){
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allScans: Flow<List<FileDetails>> = fileDao.getAllFiles()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(file: FileDetails) {
        fileDao.insert(file)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun remove(id:Int) {
        fileDao.delete(id)
    }
}