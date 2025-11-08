package com.morphox

class Routing {
    fun test(msg: String, handler: (String) -> Unit) {
        handler(msg)
    }
}

class Lambda {
    fun setup(init: Routing.() -> Unit) {
        Routing().init()
    }
}