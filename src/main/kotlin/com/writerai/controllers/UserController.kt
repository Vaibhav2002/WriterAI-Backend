package com.writerai.controllers

import com.writerai.data.models.response.Response
import com.writerai.data.repo.UserRepo

class UserController(private val repo: UserRepo) {

    suspend fun insertUser(id:String?, username: String?, email: String?) = if (id.isNullOrEmpty()|| username.isNullOrEmpty() || email.isNullOrEmpty()) {
        Response.Error("Data cannot be null or empty")
    } else {
        try {
            repo.insertUser(id, username, email)
        } catch (e: Exception) {
            Response.Error(e.message.toString())
        }
    }

    suspend fun findUser(id: String?, email: String? = null) = when {
        id.isNullOrEmpty() && email.isNullOrEmpty() -> Response.Error("Either pass id or email")
        !id.isNullOrEmpty() && !email.isNullOrEmpty() -> Response.Error("Only 1 from id or email should be passed")
        !id.isNullOrEmpty() -> repo.getUserById(id)
        else -> repo.getUserByEmail(email!!)
    }


    suspend fun deleteUser(id: String?) = if (id.isNullOrEmpty())
        Response.Error("Id cannot be empty")
    else repo.deleteUser(id)

    suspend fun getAllUsers() = repo.getAllUsers()
}