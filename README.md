<div align="center">

# ChromaFlow

**A Jetpack Compose library that renders images with a lifecycle-aware sweeping chroma (glow) animation.**

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![](https://jitpack.io/v/gyugle/ChromaFlow.svg)](https://jitpack.io/#gyugle/ChromaFlow)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://android-arsenal.com/api?level=24)

**[한국어](README-ko.md)**

<!-- Add demo GIF here -->

</div>

---

## Features

- **Sweeping glow effect** — a colored light band sweeps across the image, visible only on opaque pixels
- **Lifecycle-aware** — animation automatically pauses when the app goes to background or AOD mode, and resumes on `RESUMED`
- **Highly customizable** — glow color, base tint, direction, speed, gradient width, easing, and alpha
- **Recomposition-efficient** — animation reads only inside the Canvas draw phase, skipping full composition
- **Zero dependencies** beyond Jetpack Compose

---

## Installation

Add JitPack to your project-level `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

Then add the dependency to your module-level `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.gyugle:ChromaFlow:<version>")
}
```

> Replace `<version>` with the [latest release tag](https://github.com/gyugle/ChromaFlow/releases).

---

## Usage

### Basic

```kotlin
ChromaFlowImage(
    painter = painterResource(R.drawable.my_image),
    style = ChromaFlowStyle(glowColor = Color.Cyan),
    modifier = Modifier.fillMaxSize(),
)
```

### Full customization

```kotlin
ChromaFlowImage(
    painter = painterResource(R.drawable.my_image),
    modifier = Modifier.size(300.dp),
    style = ChromaFlowStyle(
        glowColor = Color.Cyan,
        baseColor = Color.DarkGray,
        durationMillis = 1500,
        glowAlpha = 0.8f,
        gradientWidth = 200.dp,
        direction = ChromaFlowDirection.LEFT_TO_RIGHT,
        easing = FastOutSlowInEasing,
    ),
)
```

---

## ChromaFlowStyle

| Parameter | Type | Default | Description |
|---|---|---|---|
| `glowColor` | `Color` | `Color.Unspecified` | Color of the sweeping glow. `Unspecified` disables animation. |
| `baseColor` | `Color` | `Color.Unspecified` | Tint applied over the source image. `Unspecified` keeps the original. |
| `durationMillis` | `Int` | `2500` | Duration of one sweep pass in milliseconds. |
| `glowAlpha` | `Float` | `0.9f` | Peak opacity of the glow at the gradient center (`0f`–`1f`). |
| `gradientWidth` | `Dp` | `300.dp` | Width of the sweeping gradient band. |
| `direction` | `ChromaFlowDirection` | `BIDIRECTIONAL` | Direction of the sweep. |
| `easing` | `Easing` | `LinearEasing` | Easing curve applied to each sweep pass. |

---

## ChromaFlowDirection

| Value | Description |
|---|---|
| `LEFT_TO_RIGHT` | Sweeps from left edge to right edge, then instantly resets. |
| `RIGHT_TO_LEFT` | Sweeps from right edge to left edge, then instantly resets. |
| `BIDIRECTIONAL` | Sweeps left → right → left continuously (default). |

---

## ChromaFlowDefaults

Reference defaults when partially overriding a style:

```kotlin
ChromaFlowStyle(
    glowColor = Color.Magenta,
    durationMillis = ChromaFlowDefaults.DURATION_MILLIS / 2, // twice as fast
)
```

| Constant | Value |
|---|---|
| `DURATION_MILLIS` | `2500` |
| `GLOW_ALPHA` | `0.9f` |
| `gradientWidth` | `300.dp` |
| `direction` | `ChromaFlowDirection.BIDIRECTIONAL` |
| `easing` | `LinearEasing` |

---

## Requirements

- Android **API 24+**
- Jetpack Compose

---

## License

```
Copyright 2026 Gyugle

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
