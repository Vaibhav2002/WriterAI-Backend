package com.writerai.routes

import com.writerai.controllers.BlogController
import com.writerai.data.models.requests.BlogRequest
import com.writerai.plugins.auth.FirebaseUserPrincipal
import com.writerai.utils.FIREBASE_AUTH
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.blogRoutes() {
    val controller by inject<BlogController>()
    authenticate(FIREBASE_AUTH) {
        route("/blog") {
            insertBlog(controller)
            updateBlog(controller)
            deleteBlog(controller)
            getBlog(controller)
            getAllBlogs(controller)
        }
    }
}

fun Route.insertBlog(controller: BlogController) = post("/insert") {
    val userId = call.principal<FirebaseUserPrincipal>()?.uid
    val blog = call.receive<BlogRequest>()
    controller.insertBlog(userId, blog).also {
        call.respond(it.httpStatusCode, it)
    }
}

fun Route.updateBlog(controller: BlogController) = put("/update") {
    val userId = call.principal<FirebaseUserPrincipal>()?.uid
    val blogId = call.request.queryParameters["blogId"]?.toIntOrNull()
    val blog = call.receive<BlogRequest>()
    controller.updateBlog(userId, blogId, blog).also {
        call.respond(it.httpStatusCode, it)
    }
}

fun Route.deleteBlog(controller: BlogController) = delete("/delete") {
    val userId = call.principal<FirebaseUserPrincipal>()?.uid
    val blogId = call.request.queryParameters["blogId"]?.toIntOrNull()
    controller.deleteBlog(userId, blogId).also {
        call.respond(it.httpStatusCode, it)
    }
}

fun Route.getBlog(controller: BlogController) = get("/getBlog") {
    val userId = call.principal<FirebaseUserPrincipal>()?.uid
    val blogId = call.request.queryParameters["blogId"]?.toIntOrNull()
    controller.getBlog(userId, blogId).also {
        call.respond(it.httpStatusCode, it)
    }
}

fun Route.getAllBlogs(controller: BlogController) = get("/getBlog") {
    val userId = call.principal<FirebaseUserPrincipal>()?.uid
    controller.getAllBlogs(userId).also {
        call.respond(it.httpStatusCode, it)
    }
}