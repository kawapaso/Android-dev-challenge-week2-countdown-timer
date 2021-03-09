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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.ui.screen.top.TimerState
import com.example.androiddevchallenge.ui.screen.top.TopEvent
import com.example.androiddevchallenge.ui.screen.top.TopEventDispatcher
import com.example.androiddevchallenge.ui.theme.MyTheme
import java.time.Duration

@Composable
fun Controls(
    state: TimerState,
    dispatcher: TopEventDispatcher
) {
    Row {
        Button(
            onClick = { dispatcher(TopEvent.OnClickStart) },
            modifier = Modifier.padding(8.dp),
            enabled = state is TimerState.Ready,
        ) {
            Text(text = "Start")
        }
        Button(
            onClick = { dispatcher(TopEvent.OnClickPause) },
            modifier = Modifier.padding(8.dp),
            enabled = state is TimerState.Ticking,
        ) {
            Text(text = "Pause")
        }
        Button(
            onClick = { dispatcher(TopEvent.OnClickResume) },
            modifier = Modifier.padding(8.dp),
            enabled = state is TimerState.Pausing,
        ) {
            Text(text = "Resume")
        }
        Button(
            onClick = { dispatcher(TopEvent.OnClickReset) },
            modifier = Modifier.padding(8.dp),
            enabled = state is TimerState.Pausing || state is TimerState.Reached,
        ) {
            Text(text = "Reset")
        }
    }
}

@Preview("Light Theme")
@Composable
private fun LightPreview() {
    MyTheme {
        Column {
            listOf(
                TimerState.Ready,
                TimerState.Ticking(Duration.ofSeconds(10)),
                TimerState.Pausing(Duration.ofSeconds(10)),
                TimerState.Reached
            ).forEach { Controls(it) {} }
        }
    }
}

@Preview("Dark Theme")
@Composable
private fun DarkPreview() {
    MyTheme(darkTheme = true) {
        Column {
            listOf(
                TimerState.Ready,
                TimerState.Ticking(Duration.ofSeconds(10)),
                TimerState.Pausing(Duration.ofSeconds(10)),
                TimerState.Reached
            ).forEach { Controls(it) {} }
        }
    }
}
