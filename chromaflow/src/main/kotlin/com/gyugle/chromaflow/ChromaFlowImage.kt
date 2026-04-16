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
package com.gyugle.chromaflow

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle

/**
 * Renders an image with a lifecycle-aware sweeping color (chroma) effect
 * that flows along the lines of the source image.
 *
 * The glow is implemented via a single GPU layer ([androidx.compose.ui.graphics.Canvas.saveLayer])
 * using [BlendMode.SrcIn], ensuring the effect is precisely masked to the image's opaque pixels —
 * including fine lines and edges.
 *
 * ### Lifecycle awareness
 * The animation automatically pauses when the screen enters Ambient/AOD mode or the app goes
 * to the background ([Lifecycle.State.PAUSED] or [Lifecycle.State.STOPPED]), freeing GPU/CPU
 * resources and preserving battery. It resumes when [Lifecycle.State.RESUMED] is re-entered.
 *
 * ### Recomposition efficiency
 * The animation offset is read inside the Canvas draw phase rather than at composition scope,
 * so each frame invalidates only the draw phase — not the full composition tree.
 *
 * ### Animation bounds
 * The glow sweep range is automatically derived from the actual canvas width, so the effect
 * fully covers the drawable area on any screen size or density.
 *
 * Basic usage:
 * ```kotlin
 * ChromaFlowImage(
 *     painter = painterResource(R.drawable.my_lines),
 *     style = ChromaFlowStyle(glowColor = Color.Cyan),
 *     modifier = Modifier.fillMaxSize(),
 * )
 * ```
 *
 * Customized usage:
 * ```kotlin
 * ChromaFlowImage(
 *     painter = painterResource(R.drawable.my_lines),
 *     style = ChromaFlowStyle(
 *         glowColor = Color.Red,
 *         baseColor = Color.DarkGray,
 *         durationMillis = 1500,
 *         glowAlpha = 0.8f,
 *         gradientWidth = 200.dp,
 *         direction = ChromaFlowDirection.LEFT_TO_RIGHT,
 *     ),
 *     modifier = Modifier.size(200.dp),
 * )
 * ```
 *
 * @param painter The image to render. The glow effect appears only on opaque pixels,
 *   so images with transparent backgrounds and visible lines work best.
 * @param modifier [Modifier] applied to the drawing canvas.
 * @param style Visual and animation configuration. See [ChromaFlowStyle] and [ChromaFlowDefaults].
 */
@Composable
fun ChromaFlowImage(
  painter: Painter,
  modifier: Modifier = Modifier,
  style: ChromaFlowStyle = ChromaFlowStyle(),
) {
  val offsetX = remember { Animatable(0f) }
  val lifecycleOwner = LocalLifecycleOwner.current

  // rememberUpdatedState: durationMillis and easing changes are picked up on the next
  // animation cycle without cancelling the running coroutine.
  val currentStyle by rememberUpdatedState(style)

  // Captures the actual canvas pixel width after layout, so the sweep range
  // exactly covers the drawable area on every screen size and density.
  var canvasWidthPx by remember { mutableFloatStateOf(0f) }

  val density = LocalDensity.current
  val gradientWidthPx = remember(style.gradientWidth, density) {
    with(density) { style.gradientWidth.toPx() }
  }

  // Restart when glowColor, direction, or canvas/gradient dimensions change.
  // durationMillis and easing are intentionally excluded: they are read via
  // currentStyle inside the loop so they take effect on the next cycle without
  // an abrupt animation reset.
  LaunchedEffect(lifecycleOwner, style.glowColor, style.direction, canvasWidthPx, gradientWidthPx) {
    if (currentStyle.glowColor == Color.Unspecified || canvasWidthPx <= 0f) return@LaunchedEffect

    lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
      when (currentStyle.direction) {
        ChromaFlowDirection.LEFT_TO_RIGHT -> {
          // Start fully off the left edge, sweep to fully off the right edge, then snap back.
          offsetX.snapTo(-gradientWidthPx)
          while (true) {
            offsetX.animateTo(
              targetValue = canvasWidthPx,
              animationSpec = tween(currentStyle.durationMillis, easing = currentStyle.easing),
            )
            offsetX.snapTo(-gradientWidthPx)
          }
        }

        ChromaFlowDirection.RIGHT_TO_LEFT -> {
          // Start fully off the right edge, sweep to fully off the left edge, then snap back.
          offsetX.snapTo(canvasWidthPx)
          while (true) {
            offsetX.animateTo(
              targetValue = -gradientWidthPx,
              animationSpec = tween(currentStyle.durationMillis, easing = currentStyle.easing),
            )
            offsetX.snapTo(canvasWidthPx)
          }
        }

        ChromaFlowDirection.BIDIRECTIONAL -> {
          // Sweep left → right → left continuously.
          offsetX.snapTo(-gradientWidthPx)
          while (true) {
            offsetX.animateTo(
              targetValue = canvasWidthPx,
              animationSpec = tween(currentStyle.durationMillis, easing = currentStyle.easing),
            )
            offsetX.animateTo(
              targetValue = -gradientWidthPx,
              animationSpec = tween(currentStyle.durationMillis, easing = currentStyle.easing),
            )
          }
        }
      }
    }
  }

  // Allocated once, recomputed only when the relevant style property changes.
  val neonPaint = remember { Paint() }
  val baseColorFilter = remember(style.baseColor) {
    if (style.baseColor != Color.Unspecified) {
      ColorFilter.tint(style.baseColor, BlendMode.SrcAtop)
    } else {
      null
    }
  }
  val gradientColors = remember(style.glowColor, style.glowAlpha) {
    listOf(Color.Transparent, style.glowColor.copy(alpha = style.glowAlpha), Color.Transparent)
  }

  Canvas(
    modifier = modifier.onSizeChanged { size ->
      canvasWidthPx = size.width.toFloat()
    },
  ) {
    val drawSize = size

    // Step 1: Base image — always rendered, with optional color tint.
    with(painter) {
      draw(size = drawSize, colorFilter = baseColorFilter)
    }

    // Step 2: Sweeping glow, masked to the image's opaque pixels only.
    if (style.glowColor != Color.Unspecified && canvasWidthPx > 0f) {
      // Brush is recreated each frame because it depends on the animated offsetX.
      // All other allocations are cached via remember above.
      val gradientBrush = Brush.linearGradient(
        colors = gradientColors,
        start = Offset(offsetX.value, 0f),
        end = Offset(offsetX.value + gradientWidthPx, 0f),
      )

      // Single saveLayer + SrcIn blend:
      //   1. Paint the image into the layer (establishes the alpha mask).
      //   2. Draw the gradient with SrcIn — visible only where image alpha > 0.
      //   3. Restore: the masked glow is composited over the base image (SrcOver).
      //
      // Using drawContext.canvas (Compose abstraction) rather than nativeCanvas
      // keeps the Compose save-count in sync and prevents rendering glitches.
      drawContext.canvas.saveLayer(
        Rect(0f, 0f, drawSize.width, drawSize.height),
        neonPaint,
      )
      try {
        with(painter) { draw(size = drawSize) }
        drawRect(brush = gradientBrush, blendMode = BlendMode.SrcIn)
      } finally {
        drawContext.canvas.restore()
      }
    }
  }
}
