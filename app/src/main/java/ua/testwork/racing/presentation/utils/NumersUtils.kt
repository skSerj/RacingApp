package ua.testwork.racing.presentation.utils

import kotlin.random.Random

fun getRandomFloat(from: Float, until: Float) = Random.nextFloat() * (until - from) + from