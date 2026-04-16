/*
 * Copyright 2026 Gyugle
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
