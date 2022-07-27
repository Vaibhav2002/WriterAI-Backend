package com.writerai.data.datasources.project

import com.writerai.data.db.tables.ProjectTable
import com.writerai.data.models.entities.Project
import com.writerai.data.models.entities.User
import com.writerai.data.models.requests.ProjectRequest
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

class ProjectDataSourceImpl : ProjectDataSource {


    init {
        transaction {
            SchemaUtils.create(ProjectTable)
        }
    }

    override suspend fun insertProject(userId: String, projectRequest: ProjectRequest) = newSuspendedTransaction {
        User.findById(userId) ?: return@newSuspendedTransaction null
        Project.new {
            title = projectRequest.title
            description = projectRequest.description
            content = projectRequest.content
            timeStamp = projectRequest.timeStamp
            coverPic = projectRequest.coverImage
            this.userId = userId
        }
    }

    override suspend fun updateProject(userId: String, projectId: Int, projectRequest: ProjectRequest) =
        newSuspendedTransaction {
            val project = Project.find(findProjectOfUserExp(projectId, userId)).firstOrNull()?.apply {
                content = projectRequest.content
                title = projectRequest.title
                description = projectRequest.description
                coverPic = projectRequest.coverImage
            }
            project
        }

    override suspend fun deleteProject(userId: String, projectId: Int) = newSuspendedTransaction {
        val project = Project.find(findProjectOfUserExp(projectId, userId)).firstOrNull()
        project?.delete()
        project
    }

    override suspend fun getAllProjectsOfUser(userId: String): List<Project> = newSuspendedTransaction {
        Project.find {
            ProjectTable.userId eq userId
        }.toList().sortedByDescending { it.timeStamp }
    }

    override suspend fun getAllProjects(): List<Project> = newSuspendedTransaction {
        Project.all().toList().sortedByDescending { it.timeStamp }
    }

    override suspend fun getProject(userId: String, projectId: Int) = newSuspendedTransaction {
        Project.find(findProjectOfUserExp(projectId, userId))
            .firstOrNull()
    }

    private fun findProjectOfUserExp(projectId: Int, userId: String) =
        (ProjectTable.id eq projectId) and (ProjectTable.userId eq userId)
}