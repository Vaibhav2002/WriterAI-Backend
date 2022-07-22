package com.writerai.data.models.requests

data class BlogRequest(
    val title: String,
    val description: String,
    val content: String,
    val coverImage:String,
    val timeStamp: Long
)