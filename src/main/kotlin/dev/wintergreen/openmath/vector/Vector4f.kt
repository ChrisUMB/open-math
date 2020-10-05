package dev.wintergreen.openmath.vector

import dev.wintergreen.openmath.global.degrees
import dev.wintergreen.openmath.global.radians
import dev.wintergreen.openmath.matrix.Matrix4f
import dev.wintergreen.openmath.quaternion.Quaternionf
import dev.wintergreen.openmath.util.unsafe
import java.nio.FloatBuffer
import kotlin.math.abs
import kotlin.math.sqrt

data class Vector4f(
    var x: Float = 0f,
    var y: Float = 0f,
    var z: Float = 0f,
    var w: Float = 0f
) {
    constructor(xyzw: Float) : this(xyzw, xyzw, xyzw, xyzw)

    constructor(buffer: FloatBuffer) : this(buffer.get(), buffer.get(), buffer.get(), buffer.get())

    val floatArray: FloatArray
        get() = floatArrayOf(x, y, z, w)

    val lengthSquared: Float
        get() = x * x + y * y + z * z + w * w

    val length: Float
        get() = sqrt(lengthSquared)

    val normalized: Vector4f
        get() = this / length

    fun set(x: Float, y: Float, z: Float, w: Float) {
        this.x = x
        this.y = y
        this.z = z
        this.w = w
    }

    operator fun set(index: Int, value: Float) {
        if (index > 3) {
            throw IndexOutOfBoundsException("Index $index is out of bounds for Vector4f.")
        }

        val fieldOffset = index * 4
        unsafe.putFloat(this, vector4fFirstFieldAddress + fieldOffset, value)
    }

    operator fun get(index: Int): Float {
        if (index > 3) {
            throw IndexOutOfBoundsException("Index $index is out of bounds for Vector4f.")
        }

        val fieldOffset = index * 4
        return unsafe.getFloat(this, vector4fFirstFieldAddress + fieldOffset)
    }

    operator fun plus(other: Vector4f): Vector4f {
        return Vector4f(x + other.x, y + other.y, z + other.z, w + other.w)
    }

    operator fun plus(other: Float): Vector4f {
        return Vector4f(x + other, y + other, z + other, w + other)
    }

    operator fun minus(other: Vector4f): Vector4f {
        return Vector4f(x - other.x, y - other.y, z - other.z, w - other.w)
    }

    operator fun minus(other: Float): Vector4f {
        return Vector4f(x - other, y - other, z - other, w - other)
    }

    operator fun unaryMinus(): Vector4f {
        return Vector4f(-x, -y, -z, -w)
    }

    operator fun times(other: Vector4f): Vector4f {
        return Vector4f(x * other.x, y * other.y, z * other.z, w * other.w)
    }

    operator fun times(other: Float): Vector4f {
        return Vector4f(x * other, y * other, z * other, w * other)
    }

    operator fun div(other: Vector4f): Vector4f {
        return Vector4f(x / other.x, y / other.y, z / other.z, w / other.w)
    }

    operator fun div(other: Float): Vector4f {
        return Vector4f(x / other, y / other, z / other, w / other)
    }

    operator fun rem(other: Vector4f): Vector4f {
        return Vector4f(x % other.x, y % other.y, z % other.z, w % other.w)
    }

    operator fun rem(other: Float): Vector4f {
        return Vector4f(x % other, y % other, z % other, w % other)
    }

    fun sqrt(): Vector4f {
        return Vector4f(sqrt(x), sqrt(y), sqrt(z), sqrt(w))
    }

    fun abs(): Vector4f {
        return Vector4f(abs(x), abs(y), abs(z), abs(w))
    }

    fun radians(): Vector4f {
        return Vector4f(radians(x), radians(y), radians(z), radians(w))
    }

    fun degrees(): Vector4f {
        return Vector4f(degrees(x), degrees(y), degrees(z), degrees(w))
    }

    fun distanceSquared(other: Vector4f): Float {
        return (this - other).lengthSquared
    }

    fun distance(other: Vector4f): Float {
        return (this - other).length
    }

    fun lerp(amount: Float, other: Vector4f): Vector4f {
        return this + (other - this) * amount
    }

    fun transform(matrix: Matrix4f): Vector4f {
        x = matrix.m00 * x + matrix.m01 * y + matrix.m02 * z + matrix.m03 * w
        y = matrix.m10 * x + matrix.m11 * y + matrix.m12 * z + matrix.m13 * w
        z = matrix.m20 * x + matrix.m21 * y + matrix.m22 * z + matrix.m23 * w
        w = matrix.m30 * x + matrix.m31 * y + matrix.m32 * z + matrix.m33 * w
        return this
    }

    fun dot(other: Vector4f): Float {
        return x * other.x + y * other.y + z * other.z + w * other.w
    }

    /**
     * @return a new Vector3f rotated around a unit (normalized) Quaternionf, ignoring the "w" component of the Vector4f.
     * @param other The Quaternionf in which we are rotating by.
     */
    fun rotate(other: Quaternionf): Vector4f {
        val pureQuat = Quaternionf(x, y, z, 0f)
        val rotated = other * pureQuat * other.conjugate
        return Vector4f(rotated.x, rotated.y, rotated.z, w)
    }

    operator fun iterator(): Iterator<Float> {
        return sequenceOf(x, y, z, w).iterator()
    }

    companion object {
        private val vector4fFirstFieldAddress: Long

        init {
            val xField = Vector4f::class.java.getDeclaredField("x")
            vector4fFirstFieldAddress = unsafe.objectFieldOffset(xField);
        }
    }
}