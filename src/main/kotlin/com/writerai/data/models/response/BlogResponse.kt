package com.writerai.data.models.response

@kotlinx.serialization.Serializable
data class BlogResponse(
    val id: Int,
    val title: String,
    val description: String,
    val content: String,
    val timeStamp: Long,
    val coverPic:String,
    val userId: String
)