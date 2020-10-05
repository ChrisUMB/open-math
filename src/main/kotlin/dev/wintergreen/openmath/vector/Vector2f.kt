package dev.wintergreen.openmath.vector

import dev.wintergreen.openmath.global.degrees
import dev.wintergreen.openmath.global.radians
import java.nio.FloatBuffer
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * 2 dimensional vector represented with float values.
 *
 * Operator Functionality implemented as follows:
 * plus, minus, times, divide, modulos, unaryMinus (inverse)
 */
data class Vector2f(
    var x: Float = 0f,
    var y: Float = 0f
) {
    /**
     * Alternative constructor filling both the x and the y with the parameter.
     * @param xy Value for both the x value and the y value.
     */
    constructor(xy: Float) : this(xy, xy)

    constructor(buffer: FloatBuffer) : this(buffer.get(), buffer.get())

//    /**
//     * Alternative constructor creating a Vector2f with 1 as both the x value and the y value.
//     */
//    constructor() : this(0f)

    val floatArray: FloatArray
        get() = floatArrayOf(x, y)

    /**
     * @return The length of this Vector2f squared, use this instead of length wherever possible.
     */
    val lengthSquared: Float
        get() = x * x + y * y

    /**
     * @return The length of this Vector2f. Use lengthSquared instead of this wherever possible.
     */
    val length: Float
        get() = sqrt(lengthSquared)

    /**
     * @return A new Vector2f with a magnitute (or length) of 1, while maintaining the same direction.
     */
    val normalized: Vector2f
        get() = this / length

    fun set(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    operator fun plus(other: Vector2f): Vector2f {
        return Vector2f(x + other.x, y + other.y)
    }

    operator fun plus(other: Float): Vector2f {
        return Vector2f(x + other, y + other)
    }

    operator fun minus(other: Vector2f): Vector2f {
        return Vector2f(x - other.x, y - other.y)
    }

    operator fun minus(other: Float): Vector2f {
        return Vector2f(x - other, y - other)
    }

    operator fun unaryMinus(): Vector2f {
        return Vector2f(-x, -y)
    }

    operator fun times(other: Vector2f): Vector2f {
        return Vector2f(x * other.x, y * other.y)
    }

    operator fun times(other: Float): Vector2f {
        return Vector2f(x * other, y * other)
    }

    operator fun div(other: Vector2f): Vector2f {
        return Vector2f(x / other.x, y / other.y)
    }

    operator fun div(other: Float): Vector2f {
        return Vector2f(x / other, y / other)
    }

    operator fun rem(other: Vector2f): Vector2f {
        return Vector2f(x % other.x, y % other.y)
    }

    operator fun rem(other: Float): Vector2f {
        return Vector2f(x % other, y % other)
    }

    fun sqrt(): Vector2f {
        return Vector2f(sqrt(x), sqrt(y))
    }

    fun abs(): Vector2f {
        return Vector2f(abs(x), abs(y))
    }

    fun radians(): Vector2f {
        return Vector2f(radians(x), radians(y))
    }

    fun degrees(): Vector2f {
        return Vector2f(degrees(x), degrees(y))
    }

    fun distanceSquared(other: Vector2f): Float {
        return (this - other).lengthSquared
    }

    fun distance(other: Vector2f): Float {
        return (this - other).length
    }

    fun lerp(amount: Float, other: Vector2f): Vector2f {
        return this + (other - this) * amount
    }

    fun dot(other: Vector2f): Float {
        return x * other.x + y * other.y
    }

    operator fun iterator(): Iterator<Float> {
        return sequenceOf(x, y).iterator()
    }
}


