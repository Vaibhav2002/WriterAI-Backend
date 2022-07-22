package com.writerai.data.models.response

@kotlinx.serialization.Serializable
data class UserResponse(
    val id: String,
    val username: String,
    val email: String,
    val apiReqCount:Int
)