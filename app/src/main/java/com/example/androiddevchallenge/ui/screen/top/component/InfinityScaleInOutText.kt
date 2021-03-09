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

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.tooling.preview.Preview
import com.example.androiddevchallenge.ui.theme.MyTheme

private enum class ScaleState {
    Small,
    Big,
}

@Composable
fun InfinityScaleInOutText(
    text: String,
    modifier: Modifier = Modifier
) {
    var size by remember { mutableStateOf(ScaleState.Small) }
    val transition = updateTransition(targetState = size)
    if (!transition.isRunning) {
        size = when (size) {
            ScaleState.Small -> ScaleState.Big
            ScaleState.Big -> ScaleState.Small
        }
    }

    val scale by transition.animateFloat {
        when (size) {
            ScaleState.Small -> 0.8f
            ScaleState.Big -> 1.4f
        }
    }
    Text(
        text = text,
        style = MaterialTheme.typography.h1,
        color = MaterialTheme.colors.primary,
        modifier = modifier then Modifier
            .scale(scale),
    )
}

@Preview(widthDp = 640, heightDp = 320)
@Composable
private fun Preview() {
    MyTheme {
        Box(
            contentAlignment = Alignment.Center
        ) {
            InfinityScaleInOutText(text = "Bomb!💣")
        }
    }
}
