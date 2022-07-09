package com.writerai.data.datasources.user

import com.writerai.data.models.entities.User

interface UserDataSource {
    suspend fun insertUser(username: String, email: String): User

    suspend fun getUserById(id: String): User?

    suspend fun getUserByEmail(email: String): User?

    suspend fun deleteUser(id: String): User?

    suspend fun getAllUsers(): List<User>
}