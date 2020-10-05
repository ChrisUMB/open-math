package dev.wintergreen.openmath.vector

import dev.wintergreen.openmath.global.degrees
import dev.wintergreen.openmath.global.radians
import dev.wintergreen.openmath.matrix.Matrix4f
import dev.wintergreen.openmath.quaternion.Quaternionf
import java.nio.FloatBuffer
import kotlin.math.abs
import kotlin.math.sqrt

data class Vector3f(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f
) {
    constructor(xyz: Float) : this(xyz, xyz, xyz)

    constructor(buffer: FloatBuffer) : this(buffer.get(), buffer.get(), buffer.get())

    val floatArray: FloatArray
        get() = floatArrayOf(x, y, z)

    val lengthSquared: Float
        get() = x * x + y * y + z * z

    val length: Float
        get() = sqrt(lengthSquared)

    val normalized: Vector3f
        get() = this / length

    fun set(x: Float, y: Float, z: Float) {
        this.x = x
        this.y = y
        this.z = z
    }

    operator fun plus(other: Vector3f): Vector3f {
        return Vector3f(x + other.x, y + other.y, z + other.z)
    }

    operator fun plus(other: Float): Vector3f {
        return Vector3f(x + other, y + other, z + other)
    }

    operator fun minus(other: Vector3f): Vector3f {
        return Vector3f(x - other.x, y - other.y, z - other.z)
    }

    operator fun minus(other: Float): Vector3f {
        return Vector3f(x - other, y - other, z - other)
    }

    operator fun unaryMinus(): Vector3f {
        return Vector3f(-x, -y, -z)
    }

    operator fun times(other: Vector3f): Vector3f {
        return Vector3f(x * other.x, y * other.y, z * other.z)
    }

    operator fun times(other: Float): Vector3f {
        return Vector3f(x * other, y * other, z * other)
    }

    operator fun div(other: Vector3f): Vector3f {
        return Vector3f(x / other.x, y / other.y, z / other.z)
    }

    operator fun div(other: Float): Vector3f {
        return Vector3f(x / other, y / other, z / other)
    }

    operator fun rem(other: Vector3f): Vector3f {
        return Vector3f(x % other.x, y % other.y, z % other.z)
    }

    operator fun rem(other: Float): Vector3f {
        return Vector3f(x % other, y % other, z % other)
    }

    fun sqrt(): Vector3f {
        return Vector3f(sqrt(x), sqrt(y), sqrt(z))
    }

    fun abs(): Vector3f {
        return Vector3f(abs(x), abs(y), abs(z))
    }

    fun radians(): Vector3f {
        return Vector3f(radians(x), radians(y), radians(z))
    }

    fun degrees(): Vector3f {
        return Vector3f(degrees(x), degrees(y), degrees(z))
    }

    fun dot(other: Vector3f): Float {
        return x * other.x + y * other.y + z * other.z
    }

    fun distanceSquared(other: Vector3f): Float {
        return (this - other).lengthSquared
    }

    fun distance(other: Vector3f): Float {
        return (this - other).length
    }

    fun lerp(amount: Float, other: Vector3f): Vector3f {
        return this + (other - this) * amount
    }

    fun transform(matrix: Matrix4f): Vector3f {
        x = matrix.m00 * x + matrix.m01 * y + matrix.m02 * z + matrix.m03
        y = matrix.m10 * x + matrix.m11 * y + matrix.m12 * z + matrix.m13
        z = matrix.m20 * x + matrix.m21 * y + matrix.m22 * z + matrix.m23
        return this
    }

    /**
     * @return a new Vector3f rotated around a unit (normalized) Quaternionf.
     * @param other The Quaternionf in which we are rotating by.
     */
    fun rotated(other: Quaternionf): Vector3f {
        val pureQuat = Quaternionf(x, y, z, 0f)
        val rotated = other * pureQuat * other.conjugate
        return Vector3f(rotated.x, rotated.y, rotated.z)
    }

    operator fun iterator(): Iterator<Float> {
        return sequenceOf(x, y, z).iterator()
    }
}