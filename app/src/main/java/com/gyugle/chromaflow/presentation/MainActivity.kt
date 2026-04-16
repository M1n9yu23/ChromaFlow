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
package com.gyugle.chromaflow.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import androidx.wear.compose.material3.AppScaffold
import com.gyugle.chromaflow.ChromaFlowDirection
import com.gyugle.chromaflow.ChromaFlowImage
import com.gyugle.chromaflow.ChromaFlowStyle
import com.gyugle.chromaflow.app.R
import com.gyugle.chromaflow.presentation.theme.ChromaFlowTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ChromaFlowTheme {
        SamplePager()
      }
    }
  }
}

@Composable
fun SamplePager() {
  val pagerState = rememberPagerState(pageCount = { 3 })
  AppScaffold {
    HorizontalPager(
      state = pagerState,
      beyondViewportPageCount = 1,
    ) { page ->
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
      ) {
        when (page) {
          0 -> CustomColorSample()
          1 -> NativeColorSample()
          2 -> SubtleGlowSample()
        }
      }
    }
  }
}

/**
 * Page 0 — custom glow color.
 *
 * Uses [ChromaFlowStyle.baseColor] to tint the image dark and [ChromaFlowStyle.glowColor]
 * set to magenta to match the image's existing colored lines. Shows full palette override.
 */
@Composable
fun CustomColorSample() {
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

/**
 * Page 1 — native image color glow.
 *
 * Leaves [ChromaFlowStyle.baseColor] unset to preserve the image's original dark-blue lines,
 * and sets [ChromaFlowStyle.glowColor] to a brighter version of that same blue.
 * The sweep feels like a natural light pass without changing the image's identity.
 */
@Composable
fun NativeColorSample() {
  ChromaFlowImage(
    painter = painterResource(R.drawable.img2),
    style = ChromaFlowStyle(
      glowColor = Color.White,
      durationMillis = 2500,
      glowAlpha = 0.85f,
      gradientWidth = 180.dp,
      direction = ChromaFlowDirection.BIDIRECTIONAL,
    ),
    modifier = Modifier.fillMaxSize(),
  )
}

/**
 * Page 2 — subtle glow on fine lines.
 *
 * Pairs a dark base tint with a slow, wide white sweep to highlight fine details
 * without overwhelming them. Good for images with dense or delicate line work.
 */
@Composable
fun SubtleGlowSample() {
  ChromaFlowImage(
    painter = painterResource(R.drawable.img3),
    style = ChromaFlowStyle(
      glowColor = Color.White,
      baseColor = Color.Green,
      durationMillis = 3500,
      glowAlpha = 0.7f,
      gradientWidth = 250.dp,
      direction = ChromaFlowDirection.LEFT_TO_RIGHT,
    ),
    modifier = Modifier.fillMaxSize(),
  )
}
