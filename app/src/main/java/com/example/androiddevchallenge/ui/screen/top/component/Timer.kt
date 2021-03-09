/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui.screen.top.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.screen.top.TimerState
import com.example.androiddevchallenge.ui.theme.MyTheme
import java.time.Duration

@Composable
fun Timer(state: TimerState) {
    Box(contentAlignment = Alignment.Center) {
        CountDownCircle(progress = state.progressToNextSec)
        CenterDisplay(state = state)
    }
}

@Composable
private fun CountDownCircle(progress: Float) {
    val colors = MaterialTheme.colors
    val stroke = Stroke(width = 12f)
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(24.dp)
    ) {
        drawArc(
            color = colors.secondary,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            style = stroke,
            // Roughly adjustment for good design
            alpha = (progress * 0.6).toFloat()
        )
        drawArc(
            color = colors.secondaryVariant,
            startAngle = 270f,
            sweepAngle = progress * 360,
            useCenter = false,
            style = stroke
        )
    }
}

@Composable
private fun CenterDisplay(state: TimerState) {
    when (state) {
        is TimerState.Reached -> InfinityScaleInOutText(text = "Bomb!!💣")
        is TimerState.Ready -> Ready()
        is TimerState.Pausing,
        is TimerState.Ticking -> Ticking(state = state)
    }
}

@Composable
private fun Ready() {
    Text(
        text = "Ready",
        style = MaterialTheme.typography.h1,
        color = MaterialTheme.colors.primary,
    )
}

@Composable
private fun Ticking(state: TimerState) {
    Text(
        text = "${state.nextSec}",
        fontSize = 80.sp,
        color = MaterialTheme.colors.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .alpha(state.progressToNextSec)
    )
    state.preSec?.let {
        Text(
            text = "$it",
            fontSize = 80.sp,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .alpha(1 - state.progressToNextSec)
        )
    }
}

@Preview(widthDp = 200, heightDp = 800, fontScale = 0.3f)
@Composable
private fun LightPreview() {
    MyTheme {
        Column {
            listOf(
                TimerState.Ready,
                TimerState.Ticking(Duration.ofSeconds(10)),
                TimerState.Pausing(Duration.ofSeconds(3).minusMillis(300)),
                TimerState.Reached,
            ).forEach { Timer(it) }
        }
    }
}

@Preview(widthDp = 200, heightDp = 800, fontScale = 0.3f)
@Composable
private fun DarkPreview() {
    MyTheme(darkTheme = true) {
        Column {
            listOf(
                TimerState.Ready,
                TimerState.Ticking(Duration.ofSeconds(10)),
                TimerState.Pausing(Duration.ofSeconds(3).minusMillis(300)),
                TimerState.Reached,
            ).forEach { Timer(it) }
        }
    }
}
