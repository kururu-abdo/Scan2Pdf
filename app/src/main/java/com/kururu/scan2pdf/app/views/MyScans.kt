@file:OptIn(ExperimentalFoundationApi::class)

package com.kururu.scan2pdf.app.views

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import com.kururu.scan2pdf.BuildConfig
import com.kururu.scan2pdf.PdfActivity
import com.kururu.scan2pdf.R
import com.kururu.scan2pdf.app.data.model.FileDetails
import com.kururu.scan2pdf.app.viewmodel.PdfState
import com.kururu.scan2pdf.app.viewmodel.PdfViewModel
import com.kururu.scan2pdf.app.viewmodel.ScansViewModel
import com.pspdfkit.jetpack.compose.ExperimentalPSPDFKitApi
import java.io.File


@ExperimentalFoundationApi
@OptIn(ExperimentalPSPDFKitApi::class, ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun myScans(
    
    
    viewModel: ScansViewModel , scansViewModel: ScansViewModel= androidx.lifecycle.viewmodel.compose.viewModel()){
//LaunchedEffect(key1 = true, block ={
//    viewModel.allfiles.value!!.forEach {
//        Log.v("VALUE" ,it.fileName)
//    }
//} )
//    val pdfViewModel = hiltViewModel<PdfViewModel>()
//    val state by pdfViewModel.state.collectAsState(PdfState())
val files by scansViewModel.allScans.observeAsState(arrayListOf())
  //  PdfList(state, pdfViewModel::loadPdfs, pdfViewModel::openDocument)
    Scaffold(

        topBar = {
            TopAppBar(
                backgroundColor = Color.Transparent,
                elevation = 0.dp,
                title = { Text(text = stringResource(id = R.string.Scans)) },


            )
        },
content = {
  Text(text = "")
    FileList(files ,scansViewModel)
}
        )
}

@Composable
fun FileList(files: List<FileDetails> ,viewModel: ScansViewModel) {
    LazyColumn(

    ) {
        items(files) { file ->
            FileRow(file ,viewModel)
        }

    }
}

@Composable
fun FileRow(file: FileDetails ,viewModel: ScansViewModel) {
var mContext = LocalContext.current
//    val pdfViewModel = hiltViewModel<PdfViewModel>()
//    val state by pdfViewModel.state.collectAsState(PdfState())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {

                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(FileProvider.getUriForFile(
                    mContext ,
                    BuildConfig.APPLICATION_ID + ".provider"
                     ,File(file.fileRef)
                ),
                    "application/pdf")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                val chooser = Intent.createChooser(intent, "Open with...")
                mContext.startActivity(chooser)
            }


        , content = {

          Image(painter = painterResource(id = R.drawable.pdf), contentDescription ="" ,

              modifier = Modifier
                  .width(80.dp)

                  .height(80.dp)
              )
            Spacer(modifier = Modifier.size(16.dp))
            Column(
                modifier = Modifier.weight(2F),
                content = {
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = file.fileName?.uppercase() ?: "",
                        color = Color.Black,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${file.fileDate.toString()}",
                        color = Color.Black,
                        fontSize = 14.6.sp
                    )

                })
            Spacer(modifier = Modifier.size(16.dp))
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "image",
                tint = Color.Red, modifier = Modifier
                    .size(30.dp)
                    .clickable(onClick = {
                        viewModel.remove(file.fileId!!)
                        File(file.fileRef + ".pdf").delete()

                    })
            )
        })

}
private fun ShareViaEmail(context: Context, path: String, file: File) {

    if (!file.exists()){
        Toast.makeText( context ,"File doesn't exists", Toast.LENGTH_LONG)

        return;
    }

    val uri = FileProvider.getUriForFile(
        context,
        context.applicationContext.packageName.toString() + ".provider",
        file
    )

    val intentShare = Intent(Intent.ACTION_SEND)
    intentShare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intentShare.type = "*/*"
    //intentShare.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
    intentShare.putExtra(Intent.EXTRA_STREAM,uri)
    context.startActivity(Intent.createChooser(intentShare, "Share the file ..."))
}

private fun Share(context: Context, path: String, file: File) {

    if (!file.exists()){
        Toast.makeText( context ,"File doesn't exists", Toast.LENGTH_LONG)
        return;
    }



    val intentShare = Intent(Intent.ACTION_SEND)
    intentShare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    intentShare.type = "*/*"
    intentShare.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
    //For system files: intentShare.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+path))
    context.startActivity(Intent.createChooser(intentShare, "Share the file ..."))
}

