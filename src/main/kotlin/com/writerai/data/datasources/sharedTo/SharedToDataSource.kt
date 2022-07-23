package com.writerai.data.datasources.sharedTo

import com.writerai.data.models.entities.SharedTo
import com.writerai.data.models.entities.User

interface SharedToDataSource {

    suspend fun getSharedByMe(userId: String): List<SharedTo>

    suspend fun getSharedToMe(userId: String): List<SharedTo>

    suspend fun shareTo(ownerId: String, toUser: User, blogId: Int): SharedTo

    suspend fun removeShare(userId: String, id: Int): SharedTo?

    suspend fun getSharersOfBlog(userId: String, blogId: Int): List<SharedTo>
}