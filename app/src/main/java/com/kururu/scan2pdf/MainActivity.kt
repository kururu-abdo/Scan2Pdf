package com.kururu.scan2pdf

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kururu.scan2pdf.app.utils.Destinations
import com.kururu.scan2pdf.app.viewmodel.ResultEvent
import com.kururu.scan2pdf.app.viewmodel.ResultViewModel
import com.kururu.scan2pdf.app.viewmodel.ScansViewModel
import com.kururu.scan2pdf.app.views.AdvertView
import com.kururu.scan2pdf.app.views.Dashboard
import com.kururu.scan2pdf.app.views.viewResult
import com.kururu.scan2pdf.ui.theme.Purple200
import com.kururu.scan2pdf.ui.theme.Scan2PdfTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var resultViewModel:ResultViewModel = viewModel()

            Scan2PdfTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(navController = navController, startDestination = "/"  ,route = "all") {
                        composable(Destinations.dashboard) {
                            Dashboard(navController , resultViewModel)
                        }
                        composable("result" ,

                            ) {
                            val scansViewModel = hiltViewModel<ScansViewModel>()

                                viewResult(navController = navController,
                                    resultViewModel, scansViewModel
                                )

                        }
                        composable("k") {
                           Box(modifier = Modifier.fillMaxSize() , contentAlignment = Alignment.Center) {
                               Text(text = "Android Best OS")
                           }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.P)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Home(navController: NavController ,viewModel: ResultViewModel) {

    var mContext = LocalContext.current
    val result = remember { mutableStateOf<Bitmap?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        result.value = it


        if (result.value != null){
            viewModel.onEvent(
                ResultEvent.ScanPicture(result.value!!)
            )
            navController.navigate("result")
            //navController.navigate("k")

        }
    }
    val galleryResult = remember { mutableStateOf<Bitmap?>(null) }
    var hasImage by remember {
        mutableStateOf(false)
    }
    // 2
    var imageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val galeryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            // 3
            hasImage = uri != null
            imageUri = uri

                if (hasImage && imageUri != null) {
        // 5

//                    val source = ImageDecoder
//                        .createSource(mContext.contentResolver,uri!!)
                    val bitmap2: Bitmap = MediaStore.Images.Media.getBitmap(mContext.contentResolver, Uri.parse(uri.toString()))
//                  var   bitmap = ImageDecoder.decodeBitmap(source)
                    viewModel.onEvent(
                        ResultEvent.ScanPicture(bitmap2)
                    )
                    navController.navigate("result")
    }
        }


    )

    LaunchedEffect(key1 = true, block ={
        hasImage =false
        imageUri =null
        result.value =null
    } )
//    if (hasImage && imageUri != null) {
//        // 5
//        AsyncImage(
//            model = imageUri,
//            modifier = Modifier.fillMaxWidth(),
//            contentDescription = "Selected image",
//        )
//    }
//if (result.value != null){
//    viewModel.onEvent(
//        ResultEvent.ScanPicture(result.value!!)
//    )
//  navController.navigate("result")
//   //navController.navigate("k")
//
//}
//LaunchedEffect(key1 =result.value, block = {
//    navController.navigate("/result?image=${result.value}")
//})

    Scaffold(
        content = {
            Column (modifier = Modifier.fillMaxSize()) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)

                 ,
                    contentAlignment = Alignment.Center

                ) {


//                    Text(text = "Scan to \n" +
//                            "PDF", style = TextStyle(
//
//                        fontFamily = FontFamily.Monospace ,
//                        fontWeight = FontWeight.Bold ,
//                        fontSize = 30.sp
//
//                    ))

Image(painter = painterResource(id = R.drawable.scan2pdf) , contentDescription = ""
     , modifier = Modifier .height(200.dp)
)
                }
                AdvertView()
Spacer(modifier = Modifier.fillMaxHeight(fraction = .4f))
                Row( horizontalArrangement = Arrangement.SpaceEvenly ,

                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    homeOptionITem(icon = R.drawable.gallery , onClick = {
                        galeryLauncher.launch("image/*")
                    }   , title = R.string.gallery_txt)
                    Spacer(modifier = Modifier.fillMaxWidth(fraction = .10f))
                    homeOptionITem(icon = R.drawable.camera ,

                        onClick = {
                            launcher.launch()


                        }
                     , title = R.string.camera_txt
                        )
                }



            }
        },


        )
}

@Composable
fun  homeOptionITem(icon:Int ,onClick:()->Unit , title:Int) {


    Box( contentAlignment = Alignment.Center  ,

        modifier = Modifier.padding(6.dp)
        ) {
       Card(
           elevation = 2.dp ,
           modifier = Modifier.padding(10.dp)

       ) {
          Column(
              horizontalAlignment = Alignment.CenterHorizontally
          ) {
              Image(

                  painter = painterResource(id = icon), contentDescription =""  ,

                  Modifier
                      .size(60.dp)
                      .clickable {
                          onClick()
                      }
              )
              Text(text = stringResource(id =title ))
          }
       }
    }
}