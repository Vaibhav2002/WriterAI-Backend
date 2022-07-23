package com.writerai.routes

import com.writerai.controllers.ProjectController
import com.writerai.data.models.requests.ProjectRequest
import com.writerai.plugins.auth.FirebaseUserPrincipal
import com.writerai.utils.FIREBASE_AUTH
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.projectRoutes() {
    val controller by inject<ProjectController>()
    authenticate(FIREBASE_AUTH) {
        route("/project") {
            insertProject(controller)
            updateProject(controller)
            deleteProject(controller)
            getProject(controller)
            getProjectsSharedByMe(controller)
            getProjectsSharedToMe(controller)
            shareProject(controller)
            revokeShare(controller)
        }
    }
}

fun Route.insertProject(controller: ProjectController) = post("/insert") {
    val userId = call.principal<FirebaseUserPrincipal>()?.uid
    val project = call.receive<ProjectRequest>()
    controller.insertProject(userId, project).also {
        call.respond(it.httpStatusCode, it.serialize())
    }
}

fun Route.updateProject(controller: ProjectController) = put("/update") {
    val userId = call.principal<FirebaseUserPrincipal>()?.uid
    val projectId = call.request.queryParameters["projectId"]?.toIntOrNull()
    val project = call.receive<ProjectRequest>()
    controller.updateProject(userId, projectId, project).also {
        call.respond(it.httpStatusCode, it.serialize())
    }
}

fun Route.deleteProject(controller: ProjectController) = delete("/delete") {
    val userId = call.principal<FirebaseUserPrincipal>()?.uid
    val projectId = call.request.queryParameters["projectId"]?.toIntOrNull()
    controller.deleteProject(userId, projectId).also {
        call.respond(it.httpStatusCode, it.serialize())
    }
}

fun Route.getProject(controller: ProjectController) = get("/getProject") {
    val userId = call.principal<FirebaseUserPrincipal>()?.uid
    val projectId = call.request.queryParameters["projectId"]?.toIntOrNull()
    controller.getProject(userId, projectId).also {
        call.respond(it.httpStatusCode, it.serialize())
    }
}

fun Route.getProjectsSharedByMe(controller: ProjectController) = get("/getSharedByMe") {
    val userId = call.principal<FirebaseUserPrincipal>()?.uid
    controller.getProjectsSharedByMe(userId).also {
        call.respond(it.httpStatusCode, it.serialize())
    }
}

fun Route.getProjectsSharedToMe(controller: ProjectController) = get("/getSharedToMe") {
    val userId = call.principal<FirebaseUserPrincipal>()?.uid
    controller.getProjectsSharedToMe(userId).also {
        call.respond(it.httpStatusCode, it.serialize())
    }
}

fun Route.shareProject(controller: ProjectController) = post("/share") {
    val userId = call.principal<FirebaseUserPrincipal>()?.uid
    val toEmail = call.request.queryParameters["toEmail"]
    val projectId = call.request.queryParameters["projectId"]?.toIntOrNull()
    controller.shareProject(userId, toEmail, projectId).also {
        call.respond(it.httpStatusCode, it.serialize())
    }
}

fun Route.revokeShare(controller: ProjectController) = post("/revokeShare") {
    val userId = call.principal<FirebaseUserPrincipal>()?.uid
    val shareId = call.request.queryParameters["shareId"]?.toIntOrNull()
    controller.revokeShare(userId, shareId).also {
        call.respond(it.httpStatusCode, it.serialize())
    }
}