package com.writerai.utils

import com.writerai.data.models.entities.Project
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

@JvmName("mapToUserProject")
fun Response<User>.mapToResponse() = mapTo { it.toResponse() }

@JvmName("mapToResponseProject")
fun Response<Project>.mapToResponse() = mapTo { it.toResponse() }


suspend fun <T> withUserId(userId: String?, block: suspend (String) -> Response<T>): Response<T> {
    return if (userId.isNullOrEmpty()) Response.Error(USER_ID_EMPTY)
    else block(userId)
}

suspend fun <T> withIds(
    userId: String?,
    projectId: Int?,
    block: suspend (String, Int) -> Response<T>
): Response<T> = when {
    userId.isNullOrEmpty() -> Response.Error(USER_ID_EMPTY)
    projectId == null -> Response.Error(ALL_EMPTY)
    else -> block(userId, projectId)
}