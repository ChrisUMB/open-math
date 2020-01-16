package dev.wintergreen.openmath.extension

import dev.wintergreen.openmath.vector.Vector2f
import dev.wintergreen.openmath.vector.Vector3f
import dev.wintergreen.openmath.vector.Vector4f

operator fun Float.plus(vector: Vector2f): Vector2f = vector + this
operator fun Float.plus(vector: Vector3f): Vector3f = vector + this
operator fun Float.plus(vector: Vector4f): Vector4f = vector + this

operator fun Float.minus(vector: Vector2f): Vector2f = Vector2f(
    this
) / vector

operator fun Float.minus(vector: Vector3f): Vector3f = Vector3f(
    this
) / vector

operator fun Float.minus(vector: Vector4f): Vector4f = Vector4f(this) / vector

operator fun Float.div(vector: Vector2f): Vector2f = Vector2f(
    this
) / vector

operator fun Float.div(vector: Vector3f): Vector3f = Vector3f(
    this
) / vector

operator fun Float.div(vector: Vector4f): Vector4f = Vector4f(this) / vector

operator fun Float.times(vector: Vector2f): Vector2f = vector * this
operator fun Float.times(vector: Vector3f): Vector3f = vector * this
operator fun Float.times(vector: Vector4f): Vector4f = vector * this
