package com.morphox

import com.morphox.data.*
import com.morphox.math.*
import com.morphox.rabbit.*

import redis.clients.jedis.Jedis
import kotlinx.coroutines.runBlocking

import kotlin.math.*

fun main() = runBlocking {
    testVectorDatabase()
    testRedis()
    testRabbitMQ()

    //testFirst()
    //testVector()
    //testComplex()
    //testLambdas()
}

fun testRedis() {
    Jedis("redis-server", 6379).use { jedis ->
        println("Connected to redis!")

        jedis.set("language", "Kotlin")

        val value = jedis.get("language")

        println("Read written value: $value")
    }
}

fun testRabbitMQ() {
    RMQClient("rmq", 5672).use { // Connect to RabbitMQ
        if (!it.connect("logs", "log_queue", "logs"))
        {
            println("Failed to connect to RabbitMQ")
            return
        }

        println("Connected!")

        // Listen to event
        it.onReceive { msg: String ->
            println("[Consumer1] Received: $msg")
            return@onReceive true
        }
        it.onReceive { msg: String ->
            println("[Consumer2] Received: $msg")
            return@onReceive true
        }
        it.onReceive { msg: String ->
            println("[Consumer3] Received: $msg")
            return@onReceive true
        }

        // Continuously publish to a RabbitMQ event
        while (true) {
            println("Publishing message")
            it.publish("Hello from Kotlin!")
            Thread.sleep(5000)
        }
        // client.close() // kinda useless atm
    }
}

// Vector Database concept maybe? //
fun testVectorDatabase() {
    val db = VectorDb()
    db.add("Black", 0, 0, 0)
    db.add("White", 255, 255, 255)

    db.add("Red", 255, 0, 0)
    db.add("Green", 0, 255, 0)
    db.add("Blue", 0, 0, 255)

    db.add("Yellow", 255, 255, 0)
    db.add("Magenta", 255, 0, 255)
    db.add("Cyan", 0, 255, 255)

    println(db.find(255, 255, 255)) // White
    println(db.find(255, 127, 255)) // Magenta
    println(db.find(127, 127, 255)) // Blue
    println(db.find(127, 128, 127)) // Green
    println(db.find(127, 127, 127)) // Black
    println(db.find(128, 128, 128)) // White
}

// Messing around with Kotlin some more
fun testLambda2(func: (String) -> String): String {
    return func("callback")
}

// Messing around with Kotlin some more
fun testLambda1(func: () -> Unit) {
    func()
}

fun testLambdas() {
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

    fun Int.hello() {
        println(this)
    }

    12345.hello()

    repeat(3, {
        println("hello there")
    })
}