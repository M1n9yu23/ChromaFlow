package com.gyugle.chromaflow

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Direction of the sweeping glow animation in [ChromaFlowImage].
 */
enum class ChromaFlowDirection {

    /**
     * The glow sweeps from the left edge to the right edge, then instantly resets to the left.
     * Produces a unidirectional "wipe" effect.
     */
    LEFT_TO_RIGHT,

    /**
     * The glow sweeps from the right edge to the left edge, then instantly resets to the right.
     * Produces a unidirectional "wipe" effect in the opposite direction.
     */
    RIGHT_TO_LEFT,

    /**
     * The glow sweeps left to right, then reverses back right to left, continuously.
     * This is the default behavior.
     */
    BIDIRECTIONAL,
}

/**
 * Visual and animation configuration for [ChromaFlowImage].
 *
 * All properties have sensible defaults via [ChromaFlowDefaults].
 * The minimum required to see the effect is a [glowColor]:
 * ```kotlin
 * ChromaFlowStyle(glowColor = Color.Cyan)
 * ```
 *
 * Full customization example:
 * ```kotlin
 * ChromaFlowStyle(
 *     glowColor = Color.Red,
 *     baseColor = Color.DarkGray,
 *     durationMillis = 1500,
 *     glowAlpha = 0.8f,
 *     gradientWidth = 200.dp,
 *     direction = ChromaFlowDirection.LEFT_TO_RIGHT,
 *     easing = FastOutSlowInEasing,
 * )
 * ```
 *
 * @param baseColor Tint color applied over the source image using [androidx.compose.ui.graphics.BlendMode.SrcAtop].
 *   [Color.Unspecified] leaves the image unchanged.
 * @param glowColor Color of the flowing light effect.
 *   [Color.Unspecified] disables the animation entirely, rendering only the base image.
 * @param durationMillis Duration in milliseconds for a single sweep pass.
 *   For [ChromaFlowDirection.BIDIRECTIONAL], the full cycle is [durationMillis] × 2.
 * @param glowAlpha Peak opacity of the glow at the center of the gradient band.
 *   Valid range: `0f` (invisible) to `1f` (fully opaque).
 * @param gradientWidth Width of the sweeping gradient band in density-independent pixels.
 *   Larger values produce a wider, softer glow.
 * @param direction Direction in which the glow sweeps across the image. See [ChromaFlowDirection].
 * @param easing Easing curve applied to each sweep pass. Defaults to [LinearEasing] for a
 *   constant-speed sweep. Pass a stable (singleton or remembered) [Easing] instance to avoid
 *   unnecessary animation restarts.
 */
@Immutable
data class ChromaFlowStyle(
    val baseColor: Color = Color.Unspecified,
    val glowColor: Color = Color.Unspecified,
    val durationMillis: Int = ChromaFlowDefaults.DurationMillis,
    val glowAlpha: Float = ChromaFlowDefaults.GlowAlpha,
    val gradientWidth: Dp = ChromaFlowDefaults.GradientWidth,
    val direction: ChromaFlowDirection = ChromaFlowDefaults.Direction,
    val easing: Easing = ChromaFlowDefaults.Easing,
)

/**
 * Default values for [ChromaFlowStyle] properties.
 *
 * Reference these when partially overriding a style:
 * ```kotlin
 * ChromaFlowStyle(
 *     glowColor = Color.Magenta,
 *     durationMillis = ChromaFlowDefaults.DurationMillis / 2, // twice as fast
 * )
 * ```
 */
object ChromaFlowDefaults {

    /**
     * Default duration for one sweep pass: 2500ms.
     * Full [ChromaFlowDirection.BIDIRECTIONAL] cycle: 5000ms.
     */
    const val DurationMillis: Int = 2500

    /**
     * Default peak opacity of the glow at the gradient center: 0.9 (90%).
     */
    const val GlowAlpha: Float = 0.9f

    /**
     * Default width of the sweeping gradient band: 300dp.
     */
    val GradientWidth: Dp = 300.dp

    /**
     * Default sweep direction: [ChromaFlowDirection.BIDIRECTIONAL].
     */
    val Direction: ChromaFlowDirection = ChromaFlowDirection.BIDIRECTIONAL

    /**
     * Default easing function: [LinearEasing] (constant speed).
     */
    val Easing: Easing = LinearEasing
}
