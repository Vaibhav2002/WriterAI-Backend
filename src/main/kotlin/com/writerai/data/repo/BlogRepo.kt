package com.writerai.data.repo

import com.writerai.data.datasources.blog.BlogDataSource
import com.writerai.data.models.entities.Blog
import com.writerai.data.models.requests.BlogRequest
import com.writerai.data.models.response.Response
import com.writerai.utils.safeCall

class BlogRepo(private val dataSource: BlogDataSource) {

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

    suspend fun getAllBlogs(userId: String): Response<List<Blog>> = safeCall {
        Response.Success(dataSource.getAllBlogs(userId), "All blogs fetched successfully")
    }

    suspend fun getBlog(userId: String, blogId: Int): Response<Blog> = safeCall {
        dataSource.getBlog(userId, blogId)?.let {
            Response.Success(it, "Blog fetched successfully")
        } ?: Response.Error(BLOG_NOT_FOUND)
    }
}