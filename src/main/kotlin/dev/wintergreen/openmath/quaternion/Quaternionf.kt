package dev.wintergreen.openmath.quaternion

import dev.wintergreen.openmath.matrix.Matrix3f
import dev.wintergreen.openmath.matrix.Matrix4f
import dev.wintergreen.openmath.vector.Vector3f
import java.nio.FloatBuffer
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Immutable Float Quaternion. Used to represent four dimensional rotations.
 */
data class Quaternionf(
    /**
     * First imaginary component.
     */
    val x: Float = 0f,
    /**
     * Second imaginary component.
     */
    val y: Float = 0f,
    /**
     * Third imaginary component.
     */
    val z: Float = 0f,
    /**
     * Only real component, otherwise known as "twirl".
     */
    val w: Float = 1f
) {

    constructor(buffer: FloatBuffer) : this(buffer.get(), buffer.get(), buffer.get(), buffer.get())

    val floatArray: FloatArray
        get() = floatArrayOf(x, y, z, w)

    /**
     * @return The length of this Quaternion squared, use this instead of length wherever possible.
     */
    val lengthSquared
        get() = x * x + y * y + z * z

    /**
     * @return The length of this Quaternion. Use lengthSquared instead of this wherever possible.
     */
    val length
        get() = sqrt(lengthSquared)

    /**
     * @return A new Quaternionf with normalized values, maintaining it's direction.
     * In a mathematical sense, forcing the parameters to fit inside
     * the four dimensional "unit" sphere in quaternion mathematics.
     */
    val normalized: Quaternionf
        get() {
            val d = length
            return Quaternionf(x / d, y / d, z / d, w / d)
        }

    /**
     * @return A new Quaternionf with inverted imaginary components, while maintaining the same real component.
     */
    val conjugate
        get() = Quaternionf(-x, -y, -z, w)

    /**
     * @return A Vector3f pointing in the forward direction relative to this quaternion.
     */
    val forward
        get() = Vector3f(
            2.0f * (x * z - w * y),
            2.0f * (y * z + w * x),
            1.0f - 2.0f * (x * x + y * y)
        )

    /**
     * @return A Vector3f pointing in the backward direction relative to this quaternion.
     */
    val backward
        get() = -forward

    /**
     * @return A Vector3f pointing in the up direction relative to this quaternion.
     */
    val up
        get() = Vector3f(
            2.0f * (x * y + w * z),
            1.0f - 2.0f * (x * x + z * z),
            2.0f * (y * z - w * x)
        )

    /**
     * @return A Vector3f pointing in the down direction relative to this quaternion.
     */
    val down
        get() = -up

    /**
     * @return A Vector3f pointing in the left direction relative to this quaternion.
     */
    val left
        get() = -right

    /**
     * @return A Vector3f pointing in the right direction relative to this quaternion.
     */
    val right
        get() = Vector3f(
            1.0f - 2.0f * (y * y + z * z),
            2.0f * (x * y - w * z),
            2.0f * (x * z + w * y)
        )

    /**
     * @return A Matrix3f rotated by this quaternion.
     */
    val matrix3f: Matrix3f
        inline get() {
            return Matrix3f(
                right.x, right.y, right.z,
                up.x, up.y, up.z,
                forward.x, forward.y, forward.z
            )
        }

    /**
     * @return An identity Matrix4f rotated by this quaternion.
     */
    val matrix4f: Matrix4f
        inline get() {
            return Matrix4f(
                right.x, right.y, right.z, 0f,
                up.x, up.y, up.z, 0f,
                forward.x, forward.y, forward.z, 0f,
                0f, 0f, 0f, 1f
            )
        }

    /**
     * @return Returns a Quaternionf multiplied by another Quaternionf.
     * @param other Quaternionf to multiply this Quaternionf by.
     */
    operator fun times(other: Quaternionf): Quaternionf {
        val nx = x * other.w + y * other.z - z * other.y + w * other.x
        val ny = -x * other.z + y * other.w + z * other.x + w * other.y
        val nz = x * other.y - y * other.x + z * other.w + w * other.z
        val nw = -x * other.x - y * other.y - z * other.z + w * other.w
        return Quaternionf(nx, ny, nz, nw)
    }

    /**
     * @return Returns a Quaternionf multiplied by a Vector3f.
     * @param other Vector3f to multiply this Quaternionf by.
     */
    operator fun times(other: Vector3f): Quaternionf {
        val nx = w * other.x + y * other.z - z * other.y
        val ny = w * other.y + z * other.x - x * other.z
        val nz = w * other.z + x * other.y - y * other.x
        val nw = -x * other.x - y * other.y - z * other.z
        return Quaternionf(nx, ny, nz, nw)
    }

    companion object {
        /**
         * Alternative constructor creating a Quaternionf using an axis and an angle.
         */
        operator fun invoke(axis: Vector3f, angle: Float): Quaternionf {
            val normalized = axis.normalized
            val sinHalf = sin(angle / 2f)
            val cosHalf = cos(angle / 2f)
            return Quaternionf(normalized.x * sinHalf, normalized.y * sinHalf, normalized.z * sinHalf, cosHalf)
        }

    }
}