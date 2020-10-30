package com.jeranfox.dweet

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.dispatch.withFrameMillis
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LifecycleOwnerAmbient
import androidx.lifecycle.whenStarted
import kotlin.math.cos
import kotlin.math.sin

private val COLOR = Color(0f, 0f, 0f, 0.7f)
private const val SPREAD = 3.0f
private const val SIZE = 6f
private const val SPEED = 1f / 1000f

/**
 * Stolen from: https://github.com/alexjlockwood/bees-and-bombs-compose
 */
@Composable
fun animationTimeMillis(): State<Long> {
    val millisState = mutableStateOf(0L)
    val lifecycleOwner = LifecycleOwnerAmbient.current
    LaunchedTask {
        val startTime = withFrameMillis { it }
        lifecycleOwner.whenStarted {
            while (true) {
                withFrameMillis { frameTime ->
                    millisState.value = frameTime - startTime
                }
            }
        }
    }
    return millisState
}

/**
 * Inspired by: https://www.dwitter.net
 */
@Composable
fun Dweet() {
    val millis by animationTimeMillis()
    Canvas(Modifier.fillMaxSize()) {
        val x = size.width / 2.0f / SPREAD
        val y = size.height / 2.0f / SPREAD
        val t = millis.toFloat() * SPEED
        v(this, t, x, y, 0f, 80f)
    }
}

/**
 * Modified from: https://www.dwitter.net/d/4509
 */
fun v(scope: DrawScope, t: Float, a: Float, b: Float, g: Float, r: Float) {
    if (r > 1) {
        val a2 = a + r * sin(g)
        val b2 = b - r * cos(g)
        val r2 = r * 0.7f

        scope.drawRect(COLOR, Offset(a2 * SPREAD, b2 * SPREAD), Size(SIZE, SIZE))

        v(scope, t, a2, b2, g - 1f + cos(t), r2)
        v(scope, t, a2, b2, g + 0.5f + sin(t), r2)
    }
}
