package com.writerai.data.datasources.project

import com.writerai.data.models.entities.Project
import com.writerai.data.models.requests.ProjectRequest

interface ProjectDataSource {

    suspend fun insertProject(userId: String, projectRequest: ProjectRequest): Project?

    suspend fun updateProject(userId: String, projectId: Int, projectRequest: ProjectRequest): Project?

    suspend fun deleteProject(userId: String, projectId: Int): Project?

    suspend fun getAllProjectsOfUser(userId: String): List<Project>

    suspend fun getAllProjects():List<Project>

    suspend fun getProject(userId: String, projectId: Int): Project?
}