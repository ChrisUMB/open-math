package dev.wintergreen.openmath.vector

enum class Axis(val direction: Direction) {
    X(Direction.LEFT),
    Y(Direction.UP),
    Z(Direction.BACKWARD);

    val vector = direction.vector
}