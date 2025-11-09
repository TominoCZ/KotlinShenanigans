package com.morphox.data

import com.morphox.math.*

class VectorDb() {
    data class DbResult (val value: String, val distance: Double) {
        override fun toString(): String {
            return "$value (~${"%.2f".format(distance)})"
        }
    }
    private data class DbEntry (val value: String, val vector: Vector)
    private val db: MutableList<DbEntry> = mutableListOf()

    fun add(value: String, vararg vector: Number) {
        val entry = DbEntry(
            value,
            Vector(*vector)
        )
        db.add(entry)
    }

    fun find(vararg vector: Number): DbResult? {
        val vec = Vector(*vector)
        var result: DbEntry? = null
        var distLast = Double.MAX_VALUE

        for (entry in db) {
            val dist = vec.distTo(entry.vector)
            if (dist < distLast) {
                distLast = dist
                result = entry
            }
        }

        if (result == null)
            return null

        return DbResult(result.value, distLast)
    }
}