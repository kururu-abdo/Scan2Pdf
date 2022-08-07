//package com.kururu.scan2pdf.app.utils
//
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.room.TypeConverter
//import java.text.SimpleDateFormat
//import java.time.LocalDateTime
//import java.time.ZoneOffset
//import java.time.format.DateTimeFormatter
//import java.util.*
//
//object LocalDateTimeConverter {
//    @RequiresApi(Build.VERSION_CODES.O)
//    @TypeConverter
//    fun toDate(dateString: String?): LocalDateTime? {
//        return if (dateString == null) {
//            null
//        } else {
////            val format = SimpleDateFormat("yyyy-MM-dd")
////            val date = format.parse(dateString)
//            LocalDateTime.parse(dateString)
//
////           LocalDateTime.now()
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    @TypeConverter
//    fun toDateString(date: LocalDateTime?): String? {
//        return if (date == null) {
//            null
//        } else {
////            date.toString()
////            DateFormatter()
//            var dateFormatter =DateFormatter()
//            dateFormatter.formatDate(date.atZone(ZoneOffset.UTC).toEpochSecond())
//        }
//    }
//}