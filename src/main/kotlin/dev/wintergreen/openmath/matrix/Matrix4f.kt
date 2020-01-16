package dev.wintergreen.openmath.matrix

import dev.wintergreen.openmath.global.radians
import dev.wintergreen.openmath.global.tan
import dev.wintergreen.openmath.quaternion.Quaternionf
import dev.wintergreen.openmath.util.unsafe
import dev.wintergreen.openmath.vector.Vector3f
import dev.wintergreen.openmath.vector.Vector4f
import dev.wintergreen.openmath.vector.xyz
import java.nio.Buffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Column major by default, transposed would be column major.
 */
data class Matrix4f(
    var m00: Float, var m10: Float, var m20: Float, var m30: Float,
    var m01: Float, var m11: Float, var m21: Float, var m31: Float,
    var m02: Float, var m12: Float, var m22: Float, var m32: Float,
    var m03: Float, var m13: Float, var m23: Float, var m33: Float
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
        data[8],
        data[9],
        data[10],
        data[11],
        data[12],
        data[13],
        data[14],
        data[15]
    )

    /**
     * Constructs the identity matrix, because nobody needs a zero'd out Matrix.
     */
    constructor() : this(
        1f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f,
        0f, 0f, 1f, 0f,
        0f, 0f, 0f, 1f
    )

    constructor(diagonal: Float) : this(
        diagonal, 0f, 0f, 0f,
        0f, diagonal, 0f, 0f,
        0f, 0f, diagonal, 0f,
        0f, 0f, 0f, diagonal
    )

    val buffer: ByteBuffer
        get() {
            val buffer = ByteBuffer.allocateDirect(16 * 4).order(ByteOrder.nativeOrder())
            val long = unsafe.getLong(buffer, bufferAddressFieldAddress)
            unsafe.copyMemory(this, matrix4fFirstFieldAddress, null, long, 16 * 4)
            return buffer
        }

    operator fun set(column: Int, row: Int, value: Float) {
        val fieldOffset = (row * 4 + column) * 4
        unsafe.putFloat(this, matrix4fFirstFieldAddress + fieldOffset, value)
    }

    operator fun get(column: Int, row: Int): Float {
        val fieldOffset = (row * 4 + column) * 4
        return unsafe.getFloat(this, matrix4fFirstFieldAddress + fieldOffset)
    }

    operator fun times(other: Matrix4f): Matrix4f {
        return Matrix4f(
            m00 = m00 * other.m00 + m01 * other.m10 + m02 * other.m20 + m03 * other.m30,
            m01 = m00 * other.m01 + m01 * other.m11 + m02 * other.m21 + m03 * other.m31,
            m02 = m00 * other.m02 + m01 * other.m12 + m02 * other.m22 + m03 * other.m32,
            m03 = m00 * other.m03 + m01 * other.m13 + m02 * other.m23 + m03 * other.m33,
            m10 = m10 * other.m00 + m11 * other.m10 + m12 * other.m20 + m13 * other.m30,
            m11 = m10 * other.m01 + m11 * other.m11 + m12 * other.m21 + m13 * other.m31,
            m12 = m10 * other.m02 + m11 * other.m12 + m12 * other.m22 + m13 * other.m32,
            m13 = m10 * other.m03 + m11 * other.m13 + m12 * other.m23 + m13 * other.m33,
            m20 = m20 * other.m00 + m21 * other.m10 + m22 * other.m20 + m23 * other.m30,
            m21 = m20 * other.m01 + m21 * other.m11 + m22 * other.m21 + m23 * other.m31,
            m22 = m20 * other.m02 + m21 * other.m12 + m22 * other.m22 + m23 * other.m32,
            m23 = m20 * other.m03 + m21 * other.m13 + m22 * other.m23 + m23 * other.m33,
            m30 = m30 * other.m00 + m31 * other.m10 + m32 * other.m20 + m33 * other.m30,
            m31 = m30 * other.m01 + m31 * other.m11 + m32 * other.m21 + m33 * other.m31,
            m32 = m30 * other.m02 + m31 * other.m12 + m32 * other.m22 + m33 * other.m32,
            m33 = m30 * other.m03 + m31 * other.m13 + m32 * other.m23 + m33 * other.m33
        )
    }

    operator fun timesAssign(other: Matrix4f) {
        val appliedMultiplication = this * other
        m00 = appliedMultiplication.m00
        m01 = appliedMultiplication.m01
        m02 = appliedMultiplication.m02
        m03 = appliedMultiplication.m03
        m10 = appliedMultiplication.m10
        m11 = appliedMultiplication.m11
        m12 = appliedMultiplication.m12
        m13 = appliedMultiplication.m13
        m20 = appliedMultiplication.m20
        m21 = appliedMultiplication.m21
        m22 = appliedMultiplication.m22
        m23 = appliedMultiplication.m23
        m30 = appliedMultiplication.m30
        m31 = appliedMultiplication.m31
        m32 = appliedMultiplication.m32
        m33 = appliedMultiplication.m33
    }

    fun translate(translation: Vector3f): Matrix4f {
        this *= newTranslation(translation)
        return this
    }

    fun scale(scale: Float): Matrix4f {
        return scale(Vector3f(scale))
    }

    fun scale(scale: Vector3f): Matrix4f {
        this *= newScale(scale)
        return this
    }

    fun rotate(rotation: Quaternionf): Matrix4f {
        this *= rotation.matrix4f
        return this
    }

    fun translated(translation: Vector3f): Matrix4f {
        return this * newTranslation(translation)
    }

    fun scaled(scale: Vector3f): Matrix4f {
        return this * newScale(scale)
    }

    fun rotated(rotation: Quaternionf): Matrix4f {
        return this * newRotation(rotation)
    }

    operator fun times(vector: Vector4f): Vector4f {
        return Vector4f(
            m00 * vector.x + m01 * vector.y + m02 * vector.z + m03 * vector.w,
            m10 * vector.x + m11 * vector.y + m12 * vector.z + m13 * vector.w,
            m20 * vector.x + m21 * vector.y + m22 * vector.z + m23 * vector.w,
            m30 * vector.x + m31 * vector.y + m32 * vector.z + m33 * vector.w
        )
    }

    operator fun times(vector: Vector3f): Vector3f {
        return (this * Vector4f(vector.x, vector.y, vector.z, 1f)).xyz
    }

    fun toPrettyString(): String {
        val builder = StringBuilder()
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                builder.append("${this[j, i]} ")
            }

            builder.append('\n')
        }

        return builder.toString()
    }

    companion object {
        @JvmStatic
        private val matrix4fFirstFieldAddress: Long
        @JvmStatic
        private val bufferAddressFieldAddress: Long

        val identity get() = Matrix4f()

        init {
            val matrixField = Matrix4f::class.java.getDeclaredField("m00")
            matrix4fFirstFieldAddress = unsafe.objectFieldOffset(matrixField)

            val bufferField = Buffer::class.java.getDeclaredField("address")
            bufferAddressFieldAddress = unsafe.objectFieldOffset(bufferField)
        }

        fun newRotation(rotation: Quaternionf): Matrix4f {
            return rotation.matrix4f
        }

        fun newTranslation(translation: Vector3f): Matrix4f {
            val matrix = Matrix4f()
            matrix.m03 = translation.x
            matrix.m13 = translation.y
            matrix.m23 = translation.z
            return matrix
        }

        fun newScale(scale: Vector3f): Matrix4f {
            val matrix = Matrix4f()
            matrix.m00 = scale.x
            matrix.m11 = scale.y
            matrix.m22 = scale.z
            return matrix
        }

        fun perspective(aspectRatio: Float, fov: Float, zNear: Float, zFar: Float): Matrix4f {
            val zRange = zNear - zFar
            val tanHalfFOV = tan(radians(fov) / 2f)
            return Matrix4f(
                1f / (tanHalfFOV * aspectRatio), 0f, 0f, 0f,
                0f, 1f / tanHalfFOV, 0f, 0f,
                0f, 0f, (-zNear - zFar) / zRange, 1f,
                0f, 0f, 2f * zFar * zNear / zRange, 0f
            )
        }

        fun orthographic(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): Matrix4f {
            val ortho = Matrix4f()

            val tx = -(right + left) / (right - left)
            val ty = -(top + bottom) / (top - bottom)
            val tz = -(far + near) / (far - near)
            ortho.m00 = 2f / (right - left)
            ortho.m11 = 2f / (top - bottom)
            ortho.m22 = -2f / (far - near)
            ortho.m03 = tx
            ortho.m13 = ty
            ortho.m23 = tz

            return ortho
        }
    }
}