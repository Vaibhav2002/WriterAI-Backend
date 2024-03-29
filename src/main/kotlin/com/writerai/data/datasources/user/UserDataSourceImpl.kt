package com.writerai.data.datasources.user

import com.writerai.data.db.tables.UserTable
import com.writerai.data.models.entities.User
import kotlinx.coroutines.Dispatchers.IO
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class UserDataSourceImpl : UserDataSource {

    init {
        transaction { SchemaUtils.create(UserTable) }
    }

    override suspend fun insertUser(id: String, username: String, email: String): User = newSuspendedTransaction(IO) {
        User.new(id) {
            this.username = username
            this.email = email
            this.totalApiReqMade = 0
        }
    }

    override suspend fun increaseUserApiReq(id: String, incBy: Int): User? = newSuspendedTransaction {
        User.findById(id)?.apply {
            totalApiReqMade += incBy
        }
    }

    override suspend fun getUserById(id: String): User? = newSuspendedTransaction(IO) {
        User.findById(id)
    }

    override suspend fun getUserByEmail(email: String): User? = newSuspendedTransaction(IO) {
        User.find {
            UserTable.email eq email
        }.firstOrNull()
    }

    override suspend fun deleteUser(id: String): User? = newSuspendedTransaction {
        val user = User.findById(id)
        user?.delete()
        user
    }

    override suspend fun getAllUsers(): List<User> = newSuspendedTransaction {
        User.all().toList()
    }
}