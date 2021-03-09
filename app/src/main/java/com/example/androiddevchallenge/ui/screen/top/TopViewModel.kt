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
package com.example.androiddevchallenge.ui.screen.top

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddevchallenge.util.PausableTimer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.Duration

private val MAX_COUNT = Duration.ofSeconds(3)

/**
 *  ViewModel
 *  event↑   ↓state
 *  composable UI
 */
class TopViewModel : ViewModel() {
    @Composable
    fun unapply(): Pair<TopState, TopEventDispatcher> {
        val state by state.collectAsState()
        return state to ::dispatchEvent
    }

    private val state = MutableStateFlow(TopState.DEFAULT)

    private fun dispatchEvent(e: TopEvent) {
        viewModelScope.launch {
            val currentValue = state.value.timer.remain
            when (e) {
                TopEvent.OnClickPause -> {
                    timer.pause()
                    state.emit(TopState(TimerState.Pausing(currentValue)))
                }
                TopEvent.OnClickReset -> {
                    timer.reset()
                    state.emit(TopState(TimerState.Ready))
                }
                TopEvent.OnClickResume -> {
                    timer.resume()
                    state.emit(TopState(TimerState.Ticking(currentValue)))
                }
                TopEvent.OnClickStart -> {
                    timer.start()
                    state.emit(TopState(TimerState.Ticking(MAX_COUNT)))
                }
            }
        }
    }

    private val timer = PausableTimer(
        maxMills = MAX_COUNT.toMillis(),
        onTick = {
            viewModelScope.launch {
                state.emit(TopState(TimerState.Ticking(Duration.ofMillis(it))))
            }
        },
        onFinish = {
            viewModelScope.launch {
                state.emit(TopState(TimerState.Reached))
            }
        }
    )
}

/**
 * Event
 */

typealias TopEventDispatcher = (TopEvent) -> Unit

sealed class TopEvent {
    object OnClickStart : TopEvent()
    object OnClickPause : TopEvent()
    object OnClickResume : TopEvent()
    object OnClickReset : TopEvent()
}

/**
 * State
 */

data class TopState(val timer: TimerState) {
    companion object {
        val DEFAULT = TopState(TimerState.Ready)
    }
}

sealed class TimerState {
    abstract val remain: Duration

    val progressToNextSec: Float
        get() = 1f - (remain.minusSeconds(remain.seconds).toMillis() / 1000f)
    val nextSec: Int get() = remain.seconds.toInt() + 1
    val preSec: Int? get() = (nextSec + 1).takeIf { Duration.ofSeconds(it.toLong()) <= MAX_COUNT }

    object Ready : TimerState() {
        override val remain: Duration = MAX_COUNT
    }

    data class Ticking(override val remain: Duration) : TimerState()
    data class Pausing(override val remain: Duration) : TimerState()
    object Reached : TimerState() {
        override val remain: Duration = Duration.ZERO
    }
}
