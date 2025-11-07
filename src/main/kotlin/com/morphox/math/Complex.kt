package com.morphox.math

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class Complex(var real: Double = 0.0, var imag: Double = 0.0) {
    companion object {
        fun fromAngle(rad: Double = 0.0, mag: Double = 1.0): Complex {
            return Complex(cos(rad) * mag, sin(rad) * mag)
        }
    }

    fun mag(): Double {
        return sqrt(real*real + imag*imag)
    }

    fun unit(): Complex {
        val mag = mag()
        val r = if (mag <= 0.000001) 0.0 else (real / mag)
        val i = if (mag <= 0.000001) 0.0 else (imag / mag)

        return Complex(r, i)
    }

    fun dot(other: Complex): Double {
        return this.real * other.real + this.imag * other.imag
    }

    fun toAngle(): Double {
        val unit = unit()

        return atan2(unit.imag, unit.real)
    }

    fun clone(): Complex {
        return Complex(real, imag)
    }

    override fun toString(): String {
        if (imag == 0.0)
            return "$real"
        if (real == 0.0)
            return "${imag}i"

        return "$real + ${imag}i"
    }

    operator fun times(other: Complex): Complex {
        return Complex(
            this.real - (this.imag * other.imag),
            other.real * this.imag + other.imag * this.real)
    }

    operator fun plus(other: Complex): Complex {
        return Complex(this.real + other.real, this.imag + other.imag)
    }

    operator fun minus(other: Complex): Complex {
        return Complex(this.real - other.real, this.imag - other.imag)
    }

    operator fun unaryMinus(): Complex {
        return Complex(-this.real, -this.imag)
    }
}