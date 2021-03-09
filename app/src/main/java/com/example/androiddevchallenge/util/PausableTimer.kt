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
package com.example.androiddevchallenge.util

import android.os.CountDownTimer

class PausableTimer(
    private val maxMills: Long,
    private val onTick: (Long) -> Unit,
    private val onFinish: () -> Unit
) {
    private var timer = countDownTimer(maxMills)
    private var currentValue: Long = maxMills

    fun start() {
        timer.cancel()
        timer = countDownTimer(maxMills)
        timer.start()
    }

    fun pause() {
        timer.cancel()
    }

    fun resume() {
        timer.cancel()
        timer = countDownTimer(currentValue)
        timer.start()
    }

    fun reset() {
        timer.cancel()
        timer = countDownTimer(currentValue)
    }

    private fun countDownTimer(startValue: Long): CountDownTimer =
        object : CountDownTimer(startValue, 1) {
            override fun onTick(millisUntilFinished: Long) {
                currentValue = millisUntilFinished
                this@PausableTimer.onTick(millisUntilFinished)
            }

            override fun onFinish() {
                this@PausableTimer.onFinish()
            }
        }
}
