package com.writerai.routes

import com.writerai.controllers.UserController
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoutes() {
    val controller by inject<UserController>()
    route("/user") {
        insertUser(controller)
        findUser(controller)
        deleteUser(controller)
        getAllUsers(controller)
    }
}

private fun Route.insertUser(controller: UserController) {
    post("/insert") {
        val email = call.request.queryParameters["email"]
        val username = call.request.queryParameters["username"]
        val response = controller.insertUser(username, email)
        call.respond(response.httpStatusCode, response.serialize())
    }
}

private fun Route.findUser(controller: UserController) {
    get("/find") {
        val id = call.request.queryParameters["id"]
        val email = call.request.queryParameters["email"]
        val response = controller.findUser(id, email)
        call.respond(response.httpStatusCode, response.serialize())
    }
}

private fun Route.getAllUsers(controller: UserController) {
    get("/all") {
        val response = controller.getAllUsers()
        call.respond(response.httpStatusCode, response.serialize())
    }
}

private fun Route.deleteUser(controller: UserController) {
    delete("/delete") {
        val id = call.request.queryParameters["id"]
        val response = controller.deleteUser(id)
        call.respond(response.httpStatusCode, response.serialize())
    }
}