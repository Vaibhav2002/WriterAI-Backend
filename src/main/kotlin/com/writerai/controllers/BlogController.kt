package com.writerai.controllers

import com.writerai.data.models.requests.BlogRequest
import com.writerai.data.models.response.Response
import com.writerai.data.repo.BlogRepo
import com.writerai.utils.mapTo
import com.writerai.utils.mapToResponse

class BlogController(private val repo: BlogRepo) {

    companion object {
        private const val ALL_EMPTY = "Neither of the IDs can be empty"
        private const val USER_ID_EMPTY = "User id cannot be empty"
    }

    suspend fun insertBlog(userId: String?, blogRequest: BlogRequest) =
        if (userId.isNullOrEmpty()) Response.Error(USER_ID_EMPTY)
        else repo.insertBlog(userId, blogRequest).mapToResponse()

    suspend fun updateBlog(
        userId: String?,
        blogId: Int?, blogRequest: BlogRequest
    ) = if (userId.isNullOrEmpty() || blogId == null) Response.Error(ALL_EMPTY)
    else repo.updateBlog(userId, blogId, blogRequest).mapToResponse()


    suspend fun deleteBlog(userId: String?, blogId: Int?) =
        if (userId.isNullOrEmpty() || blogId == null) Response.Error(ALL_EMPTY)
        else repo.deleteBlog(userId, blogId)

    suspend fun getBlog(userId: String?, blogId: Int?) = when {
        userId.isNullOrEmpty() -> Response.Error(USER_ID_EMPTY)
        blogId == null -> repo.getAllBlogs(userId).mapTo { blogs -> blogs.map { it.toResponse() } }
        else -> repo.getBlog(userId, blogId).mapToResponse()
    }
}