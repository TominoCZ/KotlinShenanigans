package com.morphox

import com.morphox.math.*
import javax.print.DocFlavor
import kotlin.math.PI

fun main() {
    //testFirst()
    //testVector()
    //testComplex()
    testLambda1 {
        println("hello")

        return@testLambda1
    }
    val returned = testLambda2 { message ->
        "Returned value, $message"
        //return@testLambda2 "Returned value, $message" // Alternative
    }
    println(returned)

    val la = Lambda()
    la.setup {
        test("hello world") { msg ->
            println(msg)
        }
    }
}

// Messing around with Kotlin some more
fun testLambda2(func: (String) -> String): String {
    return func("callback")
}

// Messing around with Kotlin some more
fun testLambda1(func: () -> Unit) {
    func()
}

fun testComplex() {
    var c1 = Complex(0.0, 1.0)
    var c2 = Complex(0.0, 1.0)

    println("c1: $c1");
    println("c2: $c2");
    println("multiplied: ${c1*c2}");

    c1 = Complex(1.0, 1.0)
    c2 = Complex(1.0, 1.0)

    println("-")

    println("c1: $c1");
    println("c2: $c2");
    println("multiplied: ${c1*c2}");

    println("-")
    println("Complex.fromAngle(angle=0, mag=1.0): ${Complex.fromAngle(0.0, 1.0)}");
    println("Complex.fromAngle(angle=270, mag=2.0): ${Complex.fromAngle(PI * 1.5, 2.0)}");
    println("${Complex.fromAngle(0.0, 1.0).toAngle()}");
    println("${Complex.fromAngle(PI * 1.5, 2.0).toAngle()}");

    println("---")
}

fun testVector() {
    var vec1 = Vector(4.0, -4.0)
    var vec2 = Vector(4.0, -4.0, 2.0, 2.0)

    println("vec1: $vec1");
    println("vec2: $vec2");
    println("-")
    println("vec1 mag: ${vec1.mag()}");
    println("vec2 mag: ${vec2.mag()}");
    println("-")
    println("vec1 - vec2: ${vec1 - vec2}")
    println("vec1 + vec2: ${vec1 + vec2}")
    println("-")
    println("mag (vec1 + vec2): ${(vec1 + vec2).mag()}");
    println("dist (vec1 -> vec2): ${(vec1 - vec2).mag()}")
    println("---")

    vec1 = Vector(4.0, 4.0)
    vec2 = -vec1
    println("vec1: $vec1");
    println("vec2: $vec2");
    println("-")
    println("dot: ${vec1.dot(vec2)}");
    println("dot (unit): ${vec1.unit().dot(vec2.unit())}");
    println("---")
}

// Trying out Kotlin for the first time
fun testFirst() {
    for (i in 0..5) {
        println(i)
    }

    val array = Array(5) { 0 }
    array[0] = 1

    when (array[0]) {
        1 -> println("AYO")
        else -> {
            println("mmmmm...")
        }
    }
}