package com.writerai.controllers

import com.writerai.data.models.requests.ProjectRequest
import com.writerai.data.models.response.Response
import com.writerai.data.repo.ProjectRepo
import com.writerai.data.repo.UserRepo
import com.writerai.utils.USER_ID_EMPTY
import com.writerai.utils.mapToResponse
import com.writerai.utils.withIds
import com.writerai.utils.withUserId

class ProjectController(private val repo: ProjectRepo, private val userRepo: UserRepo) {

    suspend fun insertProject(userId: String?, projectRequest: ProjectRequest) = withUserId(userId) {
        repo.insertProject(it, projectRequest).mapToResponse()
    }

    suspend fun updateProject(
        userId: String?,
        projectId: Int?, projectRequest: ProjectRequest
    ) = withIds(userId, projectId) { userID, projectID ->
        repo.updateProject(userID, projectID, projectRequest)
    }


    suspend fun deleteProject(userId: String?, projectId: Int?) =
        withIds(userId, projectId) { userID, projectID ->
            repo.deleteProject(userID, projectID)
        }

    suspend fun getProject(userId: String?, projectId: Int?) = when {
        userId.isNullOrEmpty() -> Response.Error(USER_ID_EMPTY)
        projectId == null -> repo.getAllProjects(userId)
        else -> repo.getProject(userId, projectId)
    }

    suspend fun getProjectsSharedByMe(userId: String?) = withUserId(userId) {
        repo.getProjectsSharedByMe(it)
    }

    suspend fun getProjectsSharedToMe(userId: String?) = withUserId(userId) {
        repo.getProjectsSharedToMe(it)
    }

    suspend fun shareProject(userId: String?, toUserEmail: String?, projectId: Int?) =
        withIds(userId, projectId) { userID, projectID ->
            if (toUserEmail.isNullOrEmpty())
                return@withIds Response.Error("Email cannot be blank")
            val user = userRepo.getUserByEmail(toUserEmail)
            if (user is Response.Error) Response.Error("User cannot be found")
            else repo.shareProject(userID, user.data!!, projectID)
        }

    suspend fun revokeShare(userId: String?, sharedToId: Int?) = withIds(userId, sharedToId) { uid, sharedId ->
        repo.revokeShare(uid, sharedId)

    }
}
