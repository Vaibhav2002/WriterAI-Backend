package com.writerai.data.repo

import com.writerai.data.datasources.user.UserDataSource
import com.writerai.data.models.entities.User
import com.writerai.data.models.response.Response
import com.writerai.utils.safeCall

class UserRepo(private val userDataSource: UserDataSource) {

    suspend fun insertUser(id: String, username: String, email: String): Response<User> = safeCall {
        val user = userDataSource.insertUser(id, username, email)
        Response.Success(user, "User Saved successfully")
    }

    suspend fun getUserById(id: String): Response<User> = safeCall {
        userDataSource.getUserById(id)?.let {
            Response.Success(it, "User fetched successfully")
        } ?: Response.Error("User does not exist")
    }

    suspend fun getUserByEmail(email: String): Response<User> = safeCall {
        userDataSource.getUserByEmail(email)?.let {
            Response.Success(it, "User fetched successfully")
        } ?: Response.Error("User does not exist")
    }

    suspend fun deleteUser(id: String): Response<Unit> = safeCall {
        userDataSource.deleteUser(id)?.let {
            Response.Success(Unit, "User deleted successfully")
        } ?: Response.Error("User does not exist")
    }

    suspend fun getAllUsers(): Response<List<User>> = safeCall {
        Response.Success(
            userDataSource.getAllUsers()
        )
    }
}