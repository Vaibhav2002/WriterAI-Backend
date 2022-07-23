package com.writerai.data.repo

import com.writerai.data.datasources.blog.BlogDataSource
import com.writerai.data.datasources.sharedTo.SharedToDataSource
import com.writerai.data.models.entities.Blog
import com.writerai.data.models.entities.SharedTo
import com.writerai.data.models.entities.User
import com.writerai.data.models.requests.BlogRequest
import com.writerai.data.models.response.BlogResponse
import com.writerai.data.models.response.Response
import com.writerai.data.models.response.SharedToResponse
import com.writerai.utils.safeCall

class BlogRepo(
    private val dataSource: BlogDataSource,
    private val sharedToDataSource: SharedToDataSource
) {

    companion object {
        private const val BLOG_NOT_FOUND = "Could not find blog with this id of this user"
    }

    suspend fun insertBlog(userId: String, blogRequest: BlogRequest): Response<Blog> = safeCall {
        dataSource.insertBlog(userId, blogRequest)?.let {
            Response.Success(it, "Blog saved successfully")
        } ?: Response.Error("User with this id does not exist")
    }

    suspend fun updateBlog(
        userId: String,
        blogId: Int, blogRequest: BlogRequest
    ): Response<Blog> = safeCall {
        dataSource.updateBlog(userId, blogId, blogRequest)?.let {
            Response.Success(it, "Blog updated successfully")
        } ?: Response.Error(BLOG_NOT_FOUND)
    }

    suspend fun deleteBlog(userId: String, blogId: Int): Response<Unit> = safeCall {
        dataSource.deleteBlog(userId, blogId)?.let {
            Response.Success(Unit, "Blog deleted successfully")
        } ?: Response.Error(BLOG_NOT_FOUND)
    }

    suspend fun getAllBlogs(userId: String): Response<List<BlogResponse>> = safeCall {
        val blogResponse = mergeBlogWithSharedTo(
            dataSource.getAllBlogs(userId),
            sharedToDataSource.getSharedByMe(userId)
        )
        Response.Success(blogResponse, "All blogs fetched successfully")
    }

    suspend fun getBlog(userId: String, blogId: Int): Response<BlogResponse> = safeCall {
        val sharers = sharedToDataSource.getSharersOfBlog(userId, blogId).map { it.toResponse() }
        dataSource.getBlog(userId, blogId)?.let {
            Response.Success(it.toResponse(sharers), "Blog fetched successfully")
        } ?: Response.Error(BLOG_NOT_FOUND)
    }

    suspend fun getBlogsSharedToMe(userId: String): Response<List<Blog>> = safeCall {
        val sharedToMe = sharedToDataSource.getSharedToMe(userId).map { it.blogId }
        val blogs = dataSource.getAllBlogs(userId).filter { it.id.value in sharedToMe }
        Response.Success(blogs, "Blogs shared to me")
    }

    suspend fun getBlogsSharedByMe(userId: String): Response<List<BlogResponse>> = safeCall {
        val sharedByMe = sharedToDataSource.getSharedByMe(userId)
        val sharedByMeIds = sharedByMe.map { it.blogId }
        val blogs = dataSource.getAllBlogs(userId).filter { it.id.value in sharedByMeIds }
        val blogResponse = mergeBlogWithSharedTo(
            blogs, sharedByMe
        )
        Response.Success(blogResponse, "Blogs shared by me")
    }

    suspend fun shareBlog(userId: String, toUser: User, blogId: Int): Response<SharedToResponse> = safeCall {
        dataSource.getBlog(userId, blogId).also {
            if (it == null)
                return@safeCall Response.Error("Blog does not exist")
        }
        val response = sharedToDataSource.shareTo(userId, toUser, blogId).toResponse()
        Response.Success(response, "Blog Shared Successfully")
    }

    suspend fun revokeShare(userId: String, shareId: Int): Response<Unit> = safeCall {
        sharedToDataSource.removeShare(userId, shareId)
        Response.Success(Unit, "Share Revoked Successfully")
    }
}

private fun mergeBlogWithSharedTo(blogs: List<Blog>, sharedTo: List<SharedTo>): List<BlogResponse> {
    val sharedMap = sharedTo.map { it.toResponse() }.groupBy { it.blogId }
    return blogs.map {
        it.toResponse(sharedMap[it.id.value] ?: emptyList())
    }
}