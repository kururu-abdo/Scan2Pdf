package com.kururu.scan2pdf.app.views

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kururu.scan2pdf.R
import com.kururu.scan2pdf.app.data.model.FileDetails
import com.kururu.scan2pdf.app.viewmodel.ResultViewModel
import com.kururu.scan2pdf.app.viewmodel.ScansViewModel
import com.kururu.scan2pdf.ui.theme.Purple200
import com.kururu.scan2pdf.ui.theme.Purple500
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.R)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun  viewResult(navController: NavController ,viewModel: ResultViewModel ,scansViewModel: ScansViewModel ) {
    val openDialog = remember { mutableStateOf(false)  }
var mContext  = LocalContext.current
    LaunchedEffect(key1 = true, block ={
        Log.v("VALUE" ,viewModel.bitmapState.value .bitmap!!.height.toString() +" FROM RESUKT")

    } )
    
    
    Scaffold(

        topBar = {
            TopAppBar(
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
                title = { Text(text = stringResource(id = R.string.result_app_bar_title)) },
                navigationIcon = if (navController.previousBackStackEntry != null) {
                    {
                        IconButton(onClick = {

                          //  viewModel.onEvent(ResultEvent.CleaScan)
                            navController.navigateUp()

                        }) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    }
                } else {
                    null
                }

            )
        },


        content = {
        Column( modifier = Modifier.fillMaxSize() ,
        verticalArrangement = Arrangement.SpaceBetween
            ) {
            val textState = remember { mutableStateOf(TextFieldValue()) }
            if (openDialog.value) {

                AlertDialog(
                    onDismissRequest = {
                        // Dismiss the dialog when the user clicks outside the dialog or on the back
                        // button. If you want to disable that functionality, simply use an empty
                        // onCloseRequest.
                        openDialog.value = false
                    },
                    title = {
                        Text(text = "Save File")
                    },
                    text = {
                        TextField(
                            value = textState.value,
                            onValueChange = { textState.value = it }
                        )
                    },
                    confirmButton = {
                        Button(

                            onClick = {
                            //  var   bmp = BitmapFactory.decodeResource(resources, R.drawable.android)
                               var scaledbmp = Bitmap.createScaledBitmap(viewModel.bitmapState.value.bitmap!!, 790, 1120, false)

                                generatePDF(scaledbmp  ,textState.value.text ,scansViewModel)
                                openDialog.value = false
                                Toast.makeText(mContext ,"File Saved"  ,Toast.LENGTH_SHORT).show()
                            }) {
                            Text("Ok")
                        }
                    },
                    dismissButton = {
                        Button(

                            onClick = {
                                openDialog.value = false
                            }) {
                            Text("Cancel")
                        }
                    }
                )
            }
        
            Box(modifier = Modifier
                .width(300.dp)
                .height(400.dp)
                .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ){
               Image(
                   bitmap = viewModel.bitmapState.value.bitmap!!.asImageBitmap()

                   , contentDescription = "" ,

                   modifier = Modifier.fillMaxSize()
                   )
            }
            
            
Spacer(modifier = Modifier.fillMaxHeight(fraction = .4f))
Box(contentAlignment = Alignment.Center  ,
modifier = Modifier.fillMaxWidth()
    .height(50.dp)
    ,

    ) {
    Button(

        onClick = {

            openDialog.value = true
        } ,

        modifier = Modifier.width(250.dp)
            .height(50.dp)
            .background(Purple200)
    , colors =ButtonDefaults.buttonColors(backgroundColor = Purple200)

    ) {
        Text(text = "Convert To PDF")
    }
}
        }
    })
}

@RequiresApi(Build.VERSION_CODES.R)
private fun generatePDF(scaledbmp: Bitmap, fileName:String,scansViewModel: ScansViewModel) {
    var pdfDocument: PdfDocument = PdfDocument()

    var paint: Paint = Paint()
    var title: Paint = Paint()
    var pageHeight = 1120
    var pageWidth = 792
    // we are adding page info to our PDF file
    // in which we will be passing our pageWidth,
    // pageHeight and number of pages and after that
    // we are calling it to create our PDF.
    var myPageInfo: PdfDocument.PageInfo? =
        PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

    var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

    // creating a variable for canvas
    // from our page of PDF.
    var canvas: Canvas = myPage.canvas
    canvas.drawBitmap(scaledbmp, 0F, 0F, paint)
    // two variables for paint "paint" is used
    // for drawing shapes and we will use "title"
    // for adding text in our PDF file.
    val file: File = File(Environment.getExternalStorageDirectory(), LocalDateTime.now().toString()+".pdf")

    pdfDocument.finishPage(myPage)
    try {
        pdfDocument.writeTo(FileOutputStream(file))
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatter2 = DateTimeFormatter.ISO_TIME
        val formatted = current.format(formatter2)

//        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
//        var currentDate =sdf.format(LocalDateTime.now())
//        Log.v("VALUE" ,currentDate)
        var newFile =FileDetails(
            fileName = fileName,
            fileRef = file.path,
            fileDate = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneOffset.UTC)
                .format(Instant.now())
            //LocalDateTime.parse(currentDate)
        )
        scansViewModel.insert(newFile)






        // after creating a file name we will
        // write our PDF file to that location.


      //write fill to room database


        // on below line we are displaying a toast message as PDF file generated..
//        pdfDocument.close()
    } catch (e: Exception) {
        // below line is used
        // to handle error
        Log.v("VALUE" ,e.toString())
        e.printStackTrace()


    }
    // after storing our pdf to that
    // location we are closing our PDF file.
    pdfDocument.close()
}