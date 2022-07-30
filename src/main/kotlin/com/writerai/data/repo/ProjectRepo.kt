package com.writerai.data.repo

import com.writerai.data.datasources.project.ProjectDataSource
import com.writerai.data.datasources.sharedTo.SharedToDataSource
import com.writerai.data.models.entities.Project
import com.writerai.data.models.entities.SharedTo
import com.writerai.data.models.entities.User
import com.writerai.data.models.requests.ProjectRequest
import com.writerai.data.models.response.ProjectResponse
import com.writerai.data.models.response.Response
import com.writerai.data.models.response.SharedToResponse
import com.writerai.utils.safeCall

class ProjectRepo(
    private val dataSource: ProjectDataSource,
    private val sharedToDataSource: SharedToDataSource
) {

    companion object {
        private const val BLOG_NOT_FOUND = "Could not find project with this id of this user"
    }

    suspend fun insertProject(userId: String, projectRequest: ProjectRequest): Response<Project> = safeCall {
        dataSource.insertProject(userId, projectRequest)?.let {
            Response.Success(it, "Project saved successfully")
        } ?: Response.Error("User with this id does not exist")
    }

    suspend fun updateProject(
        userId: String,
        projectId: Int, projectRequest: ProjectRequest
    ): Response<Project> = safeCall {
        val sharers = sharedToDataSource.getSharersOfProject(projectId)
        sharers.find { it.sharedTo == userId }?.let {
            dataSource.updateProject(it.ownerId, projectId, projectRequest)?.let {project->
                Response.Success(project, "Project updated successfully")
            } ?: Response.Error(BLOG_NOT_FOUND)
        } ?: Response.Error("Not shared to this user")

    }

    suspend fun deleteProject(userId: String, projectId: Int): Response<Unit> = safeCall {
        dataSource.deleteProject(userId, projectId)?.let {
            Response.Success(Unit, "Project deleted successfully")
        } ?: Response.Error(BLOG_NOT_FOUND)
    }

    suspend fun getAllProjects(userId: String): Response<List<ProjectResponse>> = safeCall {
        val projectResponse = mergeProjectWithSharedTo(
            dataSource.getAllProjectsOfUser(userId),
            sharedToDataSource.getSharedByMe(userId)
        )
        Response.Success(projectResponse, "All projects fetched successfully")
    }

    suspend fun getProject(userId: String, projectId: Int): Response<ProjectResponse> = safeCall {
        val sharers = sharedToDataSource.getSharersOfProject(projectId).map { it.toResponse() }
        dataSource.getProject(userId, projectId)?.let {
            Response.Success(it.toResponse(sharers), "Project fetched successfully")
        } ?: Response.Error(BLOG_NOT_FOUND)
    }

    suspend fun getProjectsSharedToMe(userId: String): Response<List<ProjectResponse>> = safeCall {
        val sharedToMe = sharedToDataSource.getSharedToMe(userId).map { it.projectId }
        val projects = dataSource.getAllProjects().filter { it.id.value in sharedToMe }.map { it.toResponse() }
        Response.Success(projects, "Projects shared to me")
    }

    suspend fun getProjectsSharedByMe(userId: String): Response<List<ProjectResponse>> = safeCall {
        val sharedByMe = sharedToDataSource.getSharedByMe(userId)
        val sharedByMeIds = sharedByMe.map { it.projectId }
        val projects = dataSource.getAllProjectsOfUser(userId).filter { it.id.value in sharedByMeIds }
        val projectResponse = mergeProjectWithSharedTo(
            projects, sharedByMe
        )
        Response.Success(projectResponse, "Projects shared by me")
    }

    suspend fun shareProject(userId: String, toUser: User, projectId: Int): Response<SharedToResponse> = safeCall {
        dataSource.getProject(userId, projectId).also {
            if (it == null)
                return@safeCall Response.Error("Project does not exist")
        }
        if(userId == toUser.id.value) return@safeCall Response.Error("Cannot share to self")
        sharedToDataSource.getShare(userId, toUser, projectId)?.let {
            return@safeCall Response.Error("Already shared")
        }
        val response = sharedToDataSource.shareTo(userId, toUser, projectId).toResponse()
        Response.Success(response, "Project Shared Successfully")
    }

    suspend fun revokeShare(userId: String, shareId: Int): Response<Unit> = safeCall {
        sharedToDataSource.removeShare(userId, shareId)
        Response.Success(Unit, "Share Revoked Successfully")
    }
}

private fun mergeProjectWithSharedTo(projects: List<Project>, sharedTo: List<SharedTo>): List<ProjectResponse> {
    val sharedMap = sharedTo.map { it.toResponse() }.groupBy { it.projectId }
    return projects.map {
        it.toResponse(sharedMap[it.id.value] ?: emptyList())
    }
}