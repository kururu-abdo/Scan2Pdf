package com.kururu.scan2pdf.app.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel

class ResultViewModel @Inject constructor():ViewModel() {
    private val _bitmapState = mutableStateOf(ResultState())
    val bitmapState: State<ResultState> = _bitmapState;


    fun  onEvent(resultEvent: ResultEvent){
        when(resultEvent){
            is ResultEvent.ScanPicture ->{
                Log.v("VALUE" ,resultEvent.bitmap.height.toString())
_bitmapState.value  =  ResultState(bitmap = resultEvent.bitmap)

            }
            is ResultEvent.CleaScan ->{
             try {
                 _bitmapState.value =ResultState(bitmap = null)
             }catch (e:Exception){

             }
            }
            else
                 ->{

                 }
        }
    }
}
sealed  class  ResultEvent () {
    data class  ScanPicture(var bitmap: Bitmap):ResultEvent()

    object CleaScan: ResultEvent()
}
data class ResultState (
    val bitmap: Bitmap ?=null
        )