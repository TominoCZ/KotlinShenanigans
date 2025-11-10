package com.morphox.rabbit

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback

class RMQClient : AutoCloseable {
    private var factory: ConnectionFactory? = null
    private var connection: Connection? = null
    private var channel: Channel? = null

    private var type: String = "direct"
    private var exchange: String = ""
    private var queue: String = ""
    private var key: String = ""

    private val consumers: MutableList<String> = mutableListOf()

    constructor(host: String, port: Int, user: String = "guest", pass: String = "guest") {
        factory = ConnectionFactory().apply {
            this.host = host
            this.port = port
            this.username = user
            this.password = pass
        }
    }

    private fun setup(): Boolean {
        if (channel == null || channel?.isOpen == false) {
            channel = connection?.createChannel()

            channel?.exchangeDeclare(exchange, type, true)
            channel?.queueDeclare(queue, true, false, false, null)
            channel?.queueBind(queue, exchange, key)
            channel?.basicQos(0, 1, false)
        }

        return channel?.isOpen == true
    }

    fun connect(exchange: String, queue: String, key: String, type: String = "direct"): Boolean {
        if (connection != null)
            return false

        try {
            this.type = type
            this.exchange = exchange
            this.queue = queue
            this.key = key

            connection = factory?.newConnection()

            setup()

            return connection != null && channel != null
        } catch (error: Exception) {
            println(error.stackTraceToString())
        }

        return false
    }

    fun publish(data: ByteArray) {
        if (!setup()) return

        try {
            channel?.basicPublish(exchange, key, null, data)
        } catch (e: Exception) {
            println("ERROR: Failed to publish, key=$key, ex=$exchange: ${e.stackTraceToString()}")
        }
    }

    fun publish(data: String) {
        if (!setup()) return

        try {
            channel?.basicPublish(exchange, key, null, data.toByteArray())
        } catch (e: Exception) {
            println("ERROR: Failed to publish, key=$key, ex=$exchange: ${e.stackTraceToString()}")
        }
    }

    fun onReceive(callback: (String) -> Boolean) {
        val deliverCallback = DeliverCallback { obj, delivery ->
            try {
                val message = String(delivery.body, Charsets.UTF_8)

                if (callback(message)) {
                    channel?.basicAck(delivery.envelope.deliveryTag, false)

                    return@DeliverCallback
                }
            } catch (err: Exception) {
                println("ERROR: Failed to call onReceive for $this: ${err.stackTraceToString()}")
            }

            try {
                channel?.basicNack(
                    delivery.envelope.deliveryTag,
                    false,
                    true
                ) // Last parameter could be false to move this to the Dead Letter queue
            } catch (err: Exception) {
                println("ERROR: Failed to basicNack for $this: ${err.stackTraceToString()}")
            }
        }

        if (!setup()) return

        try {
            val tag: String? = channel?.basicConsume(queue, false, deliverCallback) {}
            if (tag == null)
                return

            consumers.add(tag)
        } catch (err: Exception) {
            println("ERROR: Failed to bind basicConsume for $this: ${err.stackTraceToString()}")
        }
    }

    override fun close() {
        try {
            for (tag in consumers) {
                channel?.basicCancel(tag)
            }
            channel?.close()
            connection?.close()
        } catch (e: Exception) {
        }
        channel = null
        connection = null
    }
}