package dev.wintergreen.openmath.matrix

import dev.wintergreen.openmath.quaternion.Quaternionf
import dev.wintergreen.openmath.util.unsafe
import dev.wintergreen.openmath.vector.Vector3f
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Column major by default.
 */
data class Matrix3f(
    var m00: Float,
    var m01: Float,
    var m02: Float,
    var m10: Float,
    var m11: Float,
    var m12: Float,
    var m20: Float,
    var m21: Float,
    var m22: Float
) {

    constructor(data: FloatArray) : this(
        data[0],
        data[1],
        data[2],
        data[3],
        data[4],
        data[5],
        data[6],
        data[7],
        data[8]
    )

    /**
     * Constructs the identity matrix, because nobody needs a zero'd out Matrix.
     */
    constructor() : this(
        1f, 0f, 0f,
        0f, 1f, 0f,
        0f, 0f, 1f
    )

    constructor(diagonal: Float): this(
        diagonal, 0f, 0f,
        0f, diagonal, 0f,
        0f, 0f, diagonal
    )

    val buffer: ByteBuffer
        get() {
            val buffer = ByteBuffer.allocateDirect(9 * 4).order(ByteOrder.nativeOrder())
            val long = unsafe.getLong(buffer, bufferAddressFieldAddress)
            unsafe.copyMemory(this, matrix3fFirstFieldAddress, null, long, 9 * 4)
            return buffer
        }

    operator fun set(column: Int, row: Int, value: Float) {
        val fieldOffset = (row * 4 + column) * 4
        unsafe.putFloat(this, matrix3fFirstFieldAddress + fieldOffset, value)
    }

    operator fun get(column: Int, row: Int): Float {
        val fieldOffset = (row * 4 + column) * 4
        return unsafe.getFloat(this, matrix3fFirstFieldAddress + fieldOffset)
    }

    operator fun times(other: Matrix3f): Matrix3f {
        return Matrix3f(
            m00 = m00 * other.m00 + m01 * other.m10 + m02 * other.m20,
            m01 = m00 * other.m01 + m01 * other.m11 + m02 * other.m21,
            m02 = m00 * other.m02 + m01 * other.m12 + m02 * other.m22,
            m10 = m10 * other.m00 + m11 * other.m10 + m12 * other.m20,
            m11 = m10 * other.m01 + m11 * other.m11 + m12 * other.m21,
            m12 = m10 * other.m02 + m11 * other.m12 + m12 * other.m22,
            m20 = m20 * other.m00 + m21 * other.m10 + m22 * other.m20,
            m21 = m20 * other.m01 + m21 * other.m11 + m22 * other.m21,
            m22 = m20 * other.m02 + m21 * other.m12 + m22 * other.m22
        )
    }

    fun scale(scale: Vector3f): Matrix3f {
        m00 *= scale.x
        m11 *= scale.y
        m22 *= scale.z
        return this
    }

    fun rotate(rotation: Quaternionf): Matrix3f {
        return this * rotation.matrix3f
    }

    companion object {
        @JvmStatic
        private val matrix3fFirstFieldAddress: Long
        @JvmStatic
        private val bufferAddressFieldAddress: Long

        val identity get() = Matrix3f()

        init {
            val matrixField = Matrix3f::class.java.getDeclaredField("m00")
            matrix3fFirstFieldAddress = unsafe.objectFieldOffset(matrixField)

            val bufferField = Buffer::class.java.getDeclaredField("address")
            bufferAddressFieldAddress = unsafe.objectFieldOffset(bufferField)
        }

    }
}