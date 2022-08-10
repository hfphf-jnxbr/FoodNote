package com.example.foodnote.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.foodnote.R
import kotlin.math.pow

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun Context.showToast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    message?.let {
        Toast.makeText(this, it, length).show()
    }
}

fun Context.isNetworkAvailable(): Boolean {
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities = manager.getNetworkCapabilities(manager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
    } else {
        try {
            val activeNetworkInfo = manager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        } catch (e: Exception) {
            Log.d("isNetworkAvailable", e.message ?: "")
        }
    }
    return false
}

@Composable
fun Context.ViewComposeProgressBar() {
    val transition = rememberInfiniteTransition()
    val parameter = transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(animation = tween(durationMillis = 3000, easing = { x ->  easeInOutBack(x) })),
    )

    Card(
        modifier = Modifier.padding(all = 8.dp),
        shape = RoundedCornerShape(180.dp),
        elevation = 5.dp,
        border = BorderStroke(2.dp, Color.LightGray)
    ) {
        Column(
            Modifier.padding(all = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) { Image(
            painter = painterResource(id = R.drawable.donat_anim),
            contentDescription = "",
            modifier = Modifier
                .rotate(parameter.value)
                .size(width = 100.dp, height = 100.dp)
        )
        }
    }
}

fun easeInOutBack(x: Float) : Float {
    val c1 = 1.70158
    val c2 = c1 * 1.525

    return if(x < 0.5) {
        (((2 * x).pow(2f) * ((c2 + 1) * 2 * x - c2)) / 2).toFloat()
    }  else {
        (((2 * x - 2).pow(2f) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2).toFloat()
    }
}
