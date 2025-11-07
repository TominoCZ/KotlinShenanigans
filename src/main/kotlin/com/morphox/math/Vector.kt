package com.morphox.math

import kotlin.math.max
import kotlin.math.min
import kotlin.math.sqrt

class Vector(values: Array<Double>) {
    val axes: Int = values.size
    val values: Array<Double> = values

    constructor(vararg values: Double) : this(values.toTypedArray())
    constructor(axes: Int) : this(Array<Double>(axes) { 0.0 })

    fun mag(): Double {
        var sum = 0.0
        for (value in values) {
            sum += value * value
        }
        return sqrt(sum)
    }

    fun unit(): Vector {
        val mag = mag()
        val result = clone()

        for (index in 0..<axes) {
            result[index] = if (mag <= 0.000001) 0.0 else (result[index] / mag)
        }

        return result
    }

    fun dot(other: Vector): Double {
        val size = min(this.axes, other.axes)
        var dot = 0.0

        for (index in 0..<size) {
            dot += this.values[index] * other.values[index];
        }

        return dot;
    }

    fun clone(): Vector {
        return Vector(values.clone());
    }

    override fun toString(): String {
        return "(${values.joinToString(", ")})"
    }

    operator fun get(index: Int): Double {
        return this.values[index]
    }

    operator fun set(index: Int, value: Double) {
        this.values[index] = value
    }

    operator fun times(factor: Double): Vector {
        val result = this.clone()

        for (index in 0..<result.axes) {
            result.values[index] *= factor
        }

        return result
    }

    operator fun plus(other: Vector): Vector {
        val size = max(this.axes, other.axes)
        val result = Vector(size)

        for (index in 0..<size) {
            if (index < this.axes)
                result.values[index] = this[index]
            if (index < other.axes)
                result.values[index] += other[index]
        }

        return result
    }

    operator fun minus(other: Vector): Vector {
        val size = max(this.axes, other.axes)
        val result = Vector(size)

        for (index in 0..<size) {
            if (index < this.axes)
                result.values[index] = this[index]
            if (index < other.axes)
                result.values[index] -= other[index]
        }

        return result
    }

    operator fun unaryMinus(): Vector {
        val result = this.clone()

        for (index in 0..<this.axes) {
            result.values[index] = -result.values[index]
        }

        return result
    }
}