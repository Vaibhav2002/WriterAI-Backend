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

    override suspend fun insertUser(username: String, email: String): User = newSuspendedTransaction(IO) {
        User.new {
            this.username = username
            this.email = email
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