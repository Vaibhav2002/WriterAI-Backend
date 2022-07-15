package com.writerai.data.datasources.blog

import com.writerai.data.db.tables.BlogTable
import com.writerai.data.models.entities.Blog
import com.writerai.data.models.entities.User
import com.writerai.data.models.requests.BlogRequest
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class BlogDataSourceImpl : BlogDataSource {


    init {
        transaction {
            SchemaUtils.create(BlogTable)
        }
    }

    override suspend fun insertBlog(userId: String, blogRequest: BlogRequest) = newSuspendedTransaction {
        User.findById(userId) ?: return@newSuspendedTransaction null
        Blog.new {
            title = blogRequest.title
            description = blogRequest.description
            content = blogRequest.content
            timeStamp = blogRequest.timeStamp
            this.userId = userId
        }
    }

    override suspend fun updateBlog(userId: String, blogId: Int, blogRequest: BlogRequest) = newSuspendedTransaction {
        val blog = Blog.find(findBlogOfUserExp(blogId, userId)).firstOrNull()?.apply {
            content = blogRequest.content
            title = blogRequest.title
            description = blogRequest.description
        }
        blog
    }

    override suspend fun deleteBlog(userId: String, blogId: Int) = newSuspendedTransaction {
        val blog = Blog.find(findBlogOfUserExp(blogId, userId)).firstOrNull()
        blog?.delete()
        blog
    }

    override suspend fun getAllBlogs(userId: String): List<Blog> = newSuspendedTransaction {
        Blog.find {
            BlogTable.userId eq userId
        }.toList()
    }

    override suspend fun getBlog(userId: String, blogId: Int) = newSuspendedTransaction {
        Blog.find(findBlogOfUserExp(blogId, userId))
            .firstOrNull()
    }

    private fun findBlogOfUserExp(blogId: Int, userId: String) = (BlogTable.id eq blogId) and (BlogTable.userId eq userId)
}