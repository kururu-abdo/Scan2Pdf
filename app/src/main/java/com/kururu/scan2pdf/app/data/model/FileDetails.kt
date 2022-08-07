package com.kururu.scan2pdf.app.data.model

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime


@Entity
data class FileDetails constructor(
    @PrimaryKey(autoGenerate = true)  val fileId:Int?= null,
    val fileName:String,
    var fileRef:String,
     var fileDate:String
        ) {
}