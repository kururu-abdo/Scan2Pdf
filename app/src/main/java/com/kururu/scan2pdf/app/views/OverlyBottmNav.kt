package com.kururu.scan2pdf.app.views

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kururu.scan2pdf.R
import com.kururu.scan2pdf.ui.theme.Purple200


@SuppressLint("UnrememberedMutableState")
@Composable
fun OverlayBottomNav(navController: NavController) {
 val homeSelected = remember {
     mutableStateOf(true)
 }
    val scansSelected = remember {
        mutableStateOf(false)
    }


    Box( modifier = Modifier
        .width(
            150.dp

        )
        .height(70.dp)
        .padding(10.dp)
    //    .background(Purple200.copy(alpha = 0.5f))

        ,
        contentAlignment = Alignment.Center

    ) {
Row() {
    navItem(title = stringResource(id = R.string.home) ,selected =homeSelected.value  , onClick = {
        homeSelected.value =true
        scansSelected.value=false
        navController.navigate("home")
    })
    navItem(title = stringResource(id = R.string.Scans) ,selected =scansSelected.value ,

        onClick = {
            homeSelected.value =false
            scansSelected.value=true
            navController.navigate("scans")

        })

}
    }
}


@Composable
fun navItem(title:String ,selected:Boolean ,onClick: () ->Unit){
    Box(
        contentAlignment = Alignment.Center ,
        modifier = Modifier
            .clip(
                RoundedCornerShape(20.dp)

            )
            .background(
                if (selected) Purple200 else
                    Color.White
            )
            .padding(8.dp)
            .clickable {
                onClick()
            }
    ) {
Text(title  , style = TextStyle(
    color = if (selected) Color.White else Color.Black
))
    }
}