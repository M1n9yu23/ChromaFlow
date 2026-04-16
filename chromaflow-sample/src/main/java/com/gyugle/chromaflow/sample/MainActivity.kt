package com.gyugle.chromaflow.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gyugle.chromaflow.ChromaFlowDirection
import com.gyugle.chromaflow.ChromaFlowImage
import com.gyugle.chromaflow.ChromaFlowStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChromaFlowSample()
        }
    }
}

@Composable
fun ChromaFlowSample() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().background(Color.Black),
    ) {
        ChromaFlowImage(
            painter = painterResource(R.drawable.img),
            style = ChromaFlowStyle(
                glowColor = Color.Cyan,
                durationMillis = 2000,
                glowAlpha = 0.9f,
                gradientWidth = 200.dp,
                direction = ChromaFlowDirection.BIDIRECTIONAL,
            ),
            modifier = Modifier.fillMaxSize(),
        )
    }
}
