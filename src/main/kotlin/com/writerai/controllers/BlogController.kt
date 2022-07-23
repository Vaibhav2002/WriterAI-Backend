package com.writerai.controllers

import com.writerai.data.models.requests.BlogRequest
import com.writerai.data.models.response.Response
import com.writerai.data.repo.BlogRepo
import com.writerai.data.repo.UserRepo
import com.writerai.utils.USER_ID_EMPTY
import com.writerai.utils.mapToResponse
import com.writerai.utils.withIds
import com.writerai.utils.withUserId

class BlogController(private val repo: BlogRepo, private val userRepo: UserRepo) {

    suspend fun insertBlog(userId: String?, blogRequest: BlogRequest) = withUserId(userId) {
        repo.insertBlog(it, blogRequest).mapToResponse()
    }

    suspend fun updateBlog(
        userId: String?,
        blogId: Int?, blogRequest: BlogRequest
    ) = withIds(userId, blogId) { userID, blogID ->
        repo.updateBlog(userID, blogID, blogRequest).mapToResponse()
    }


    suspend fun deleteBlog(userId: String?, blogId: Int?) =
        withIds(userId, blogId) { userID, blogID ->
            repo.deleteBlog(userID, blogID)
        }

    suspend fun getBlog(userId: String?, blogId: Int?) = when {
        userId.isNullOrEmpty() -> Response.Error(USER_ID_EMPTY)
        blogId == null -> repo.getAllBlogs(userId)
        else -> repo.getBlog(userId, blogId)
    }

    suspend fun getBlogsSharedByMe(userId: String?) = withUserId(userId) {
        repo.getBlogsSharedByMe(it)
    }

    suspend fun getBlogsSharedToMe(userId: String?) = withUserId(userId) {
        repo.getBlogsSharedToMe(it)
    }

    suspend fun shareBlog(userId: String?, toUserEmail: String?, blogId: Int?) =
        withIds(userId, blogId) { userID, blogID ->
            if (toUserEmail.isNullOrEmpty())
                return@withIds Response.Error("Email cannot be blank")
            val user = userRepo.getUserByEmail(toUserEmail)
            if (user is Response.Error) Response.Error("User cannot be found")
            else repo.shareBlog(userID, user.data!!, blogID)
        }

    suspend fun revokeShare(userId: String?, sharedToId: Int?) = withIds(userId, sharedToId) { uid, sharedId ->
        repo.revokeShare(uid, sharedId)

    }
}
