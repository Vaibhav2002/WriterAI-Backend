package com.writerai.data.models.entities

interface Model<T> {
    fun toResponse():T
}