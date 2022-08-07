package com.kururu.scan2pdf.app.views

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState

import com.kururu.scan2pdf.Home
import com.kururu.scan2pdf.app.viewmodel.ResultViewModel
import com.kururu.scan2pdf.app.viewmodel.ScansViewModel

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalComposeUiApi::class, ExperimentalPermissionsApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun  Dashboard(nav:NavController , resultViewModel: ResultViewModel) {
    val navController = rememberNavController()

    val lifecycleOwner = LocalLifecycleOwner.current
    var mContext  = LocalContext.current

    val permissionStates = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    )

    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    permissionStates.launchMultiplePermissionRequest()
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })


    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(startDestination = "home" , navController = navController , route = "dashboard" ) {
            composable("home") { Home(nav ,resultViewModel ) }
            composable("scans") {
                val viewModel = hiltViewModel<ScansViewModel>()

                
            myScans(viewModel = viewModel)
            }
            /*...*/
        }



        Box(  modifier = Modifier.align(Alignment.BottomStart) ) {
            OverlayBottomNav(navController)
        }
    }
}


