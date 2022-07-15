package com.writerai.data.models.response

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
sealed class Response<T>(
    open val status: Int = 0,
    open val data: T? = null,
    open val message: String = ""
) {
    data class Success<T>(
        override val data: T? = null,
        override val message: String = ""
    ) : Response<T>(200, data, message)

    data class Error<T>(
        override val message: String = "",
        override val status: Int = 400,
        override val data:T? =null
    ) : Response<T>(status, data, message)

    fun serialize(): SerializedResponse<T> = SerializedResponse(status, data, message)

    val httpStatusCode
        get() = HttpStatusCode.fromValue(status)
}

@Serializable
data class SerializedResponse<T>(
    val status: Int,
    val data: T?,
    val message: String
)

