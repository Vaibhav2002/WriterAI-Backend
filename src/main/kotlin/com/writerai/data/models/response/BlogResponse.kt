package com.writerai.data.models.response

import kotlinx.serialization.Transient

@kotlinx.serialization.Serializable
data class BlogResponse(
    val id: Int,
    val title: String,
    val description: String,
    val content: String,
    val timeStamp: Long,
    val coverPic: String,
    val userId: String,
    val sharedTo: List<SharedToResponse> = emptyList()
)

@kotlinx.serialization.Serializable
data class SharedToResponse(
    @Transient
    val blogId: Int = 0,
    val email: String,
    val sharedId: Int
)