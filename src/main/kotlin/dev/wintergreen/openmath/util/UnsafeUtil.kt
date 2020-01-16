package dev.wintergreen.openmath.util

import sun.misc.Unsafe

internal val unsafe: Unsafe by lazy {
    val declaredField = Unsafe::class.java.getDeclaredField("theUnsafe")
    declaredField.isAccessible = true
    declaredField.get(null) as Unsafe
}