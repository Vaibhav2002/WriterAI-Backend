package com.writerai.controllers

import com.writerai.data.models.response.Response
import com.writerai.data.repo.UserRepo
import com.writerai.utils.mapTo
import com.writerai.utils.mapToResponse

class UserController(private val repo: UserRepo) {

    suspend fun insertUser(id: String?, username: String?, email: String?) =
        if (id.isNullOrEmpty() || username.isNullOrEmpty() || email.isNullOrEmpty()) {
            Response.Error("Data cannot be null or empty")
        } else {
            repo.insertUser(id, username, email).mapToResponse()
        }

    suspend fun findUser(id: String?, email: String? = null) = when {
        id.isNullOrEmpty() && email.isNullOrEmpty() -> Response.Error("Either pass id or email")
        !id.isNullOrEmpty() && !email.isNullOrEmpty() -> Response.Error("Only 1 from id or email should be passed")
        !id.isNullOrEmpty() -> repo.getUserById(id).mapToResponse()
        else -> repo.getUserByEmail(email!!).mapToResponse()
    }

    suspend fun incUserApiReqCount(id:String?) = if (id.isNullOrEmpty())
        Response.Error("Id cannot be empty")
    else repo.increaseUserApiReq(id).mapToResponse()

    suspend fun deleteUser(id: String?) = if (id.isNullOrEmpty())
        Response.Error("Id cannot be empty")
    else repo.deleteUser(id)

    suspend fun getAllUsers() = repo.getAllUsers() mapTo { users -> users.map { it.toResponse() } }


}