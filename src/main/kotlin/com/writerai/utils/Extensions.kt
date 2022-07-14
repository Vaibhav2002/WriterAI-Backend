package com.writerai.utils

import com.writerai.data.models.entities.Blog
import com.writerai.data.models.entities.User
import com.writerai.data.models.response.Response


suspend fun <T> safeCall(
    call: suspend () -> Response<T>
): Response<T> = try {
    call()
} catch (e: Exception) {
    Response.Error<T>(e.message.toString())
}

infix fun <T, F> Response<T>.mapTo(change: (T) -> F): Response<F> = when (this) {
    is Response.Error -> Response.Error(message, status)
    is Response.Success -> Response.Success(data?.let { change(it) }, message)
}

@JvmName("mapToUserBlog")
fun Response<User>.mapToResponse() = mapTo { it.toResponse() }

@JvmName("mapToResponseBlog")
fun Response<Blog>.mapToResponse() = mapTo { it.toResponse() }