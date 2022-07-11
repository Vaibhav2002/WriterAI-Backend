package com.writerai.data.repo

import com.writerai.data.datasources.user.UserDataSource
import com.writerai.data.models.response.Response
import com.writerai.data.models.response.UserResponse

class UserRepo(private val userDataSource: UserDataSource) {

    suspend fun insertUser(id:String, username: String, email: String): Response<UserResponse> {
        val user = userDataSource.insertUser(id, username, email).toResponse()
        return Response.Success(user, "User Saved successfully")
    }

    suspend fun getUserById(id: String): Response<UserResponse> {
        return userDataSource.getUserById(id)?.toResponse()?.let {
            Response.Success(it, "User fetched successfully")
        } ?: Response.Error("User does not exist")
    }

    suspend fun getUserByEmail(email: String): Response<UserResponse> {
        return userDataSource.getUserByEmail(email)?.toResponse()?.let {
            Response.Success(it, "User fetched successfully")
        } ?: Response.Error("User does not exist")
    }

    suspend fun deleteUser(id: String): Response<Unit> {
        return userDataSource.deleteUser(id)?.toResponse()?.let {
            Response.Success(Unit, "User deleted successfully")
        } ?: Response.Error("User does not exist")
    }

    suspend fun getAllUsers(): Response<List<UserResponse>> {
        return Response.Success(
            userDataSource.getAllUsers().map { it.toResponse() }
        )
    }
}