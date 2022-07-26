package com.writerai.data.datasources.sharedTo

import com.writerai.data.db.tables.ShareTable
import com.writerai.data.models.entities.SharedTo
import com.writerai.data.models.entities.User
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class SharedToDatasourceImpl : SharedToDataSource {

    init {
        transaction { SchemaUtils.create(ShareTable) }
    }

    override suspend fun getSharedByMe(userId: String): List<SharedTo> = newSuspendedTransaction {
        SharedTo.find {
            ShareTable.ownerId eq userId
        }.toList()
    }

    override suspend fun getSharedToMe(userId: String): List<SharedTo> = newSuspendedTransaction {
        SharedTo.find {
            ShareTable.sharedTo eq userId
        }.toList()
    }

    override suspend fun getSharersOfProject(userId: String, projectId: Int): List<SharedTo> = newSuspendedTransaction {
        SharedTo.find {
            (ShareTable.ownerId eq userId) and (ShareTable.projectId eq projectId)
        }.toList()
    }

    override suspend fun shareTo(ownerId: String, toUser: User, projectId: Int): SharedTo = newSuspendedTransaction {
        SharedTo.new {
            this.ownerId = ownerId
            this.sharedTo = toUser.id.value
            this.sharedToEmail = toUser.email
            this.projectId = projectId
        }
    }

    override suspend fun removeShare(userId: String, id: Int): SharedTo? = newSuspendedTransaction {
        val sharedTo = SharedTo.find {
            (ShareTable.ownerId eq userId) and (ShareTable.id eq id)
        }.firstOrNull()
        sharedTo?.delete()
        sharedTo
    }

    override suspend fun getShare(userId: String, toUser: User, projectId: Int): SharedTo? = newSuspendedTransaction {
        SharedTo.find {
            (ShareTable.ownerId eq userId) and (ShareTable.sharedTo eq toUser.id.value) and (ShareTable.projectId eq projectId)
        }.firstOrNull()
    }
}