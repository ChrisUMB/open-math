package dev.wintergreen.openmath.global

fun radians(value: Float) = Math.toRadians(value.toDouble()).toFloat()
fun degrees(value: Float) = Math.toDegrees(value.toDouble()).toFloat()
fun tan(value: Float) = kotlin.math.tan(value.toDouble()).toFloat()