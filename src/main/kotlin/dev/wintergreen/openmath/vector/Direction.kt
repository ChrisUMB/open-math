package dev.wintergreen.openmath.vector

enum class Direction(val vector: Vector3f) {
    UP(Vector3f(0f, 1f, 0f)),
    DOWN(Vector3f(0f, -1f, 0f)),
    LEFT(Vector3f(-1f, 0f, 0f)),
    RIGHT(Vector3f(1f, 0f, 0f)),
    FORWARD(Vector3f(0f, 0f, -1f)),
    BACKWARD(Vector3f(0f, 0f, 1f))
}