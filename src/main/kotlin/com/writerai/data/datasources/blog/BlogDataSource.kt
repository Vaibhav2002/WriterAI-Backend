package com.writerai.data.datasources.blog

import com.writerai.data.models.entities.Blog
import com.writerai.data.models.requests.BlogRequest

interface BlogDataSource {

    suspend fun insertBlog(userId:String,blogRequest: BlogRequest):Blog?

    suspend fun updateBlog(userId: String, blogId: Int, blogRequest: BlogRequest):Blog?

    suspend fun deleteBlog(userId: String, blogId: Int):Blog?

    suspend fun getAllBlogs(userId: String):List<Blog>

    suspend fun getBlog(userId: String, blogId: Int):Blog?
}