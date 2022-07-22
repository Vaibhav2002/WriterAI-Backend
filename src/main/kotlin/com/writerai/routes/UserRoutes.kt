package com.writerai.routes

import com.writerai.controllers.UserController
import com.writerai.plugins.auth.FirebaseUserPrincipal
import com.writerai.utils.FIREBASE_AUTH
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.userRoutes() {
    val controller by inject<UserController>()
    route("/user") {
        insertUser(controller)
        getUser(controller)
        deleteUser(controller)
        getAllUsers(controller)
        increaseUserApiCount(controller)
    }
}

private fun Route.insertUser(controller: UserController) {
    post("/insert") {
        val userId = call.request.queryParameters["userId"]
        val email = call.request.queryParameters["email"]
        val username = call.request.queryParameters["username"]
        val response = controller.insertUser(userId, username, email)
        call.respond(response.httpStatusCode, response.serialize())
    }
}

private fun Route.getUser(controller: UserController) = authenticate(FIREBASE_AUTH) {
    get("/getUser") {
        val userId = call.principal<FirebaseUserPrincipal>()?.uid ?: return@get
        val response = controller.findUser(userId)
        call.respond(response.httpStatusCode, response.serialize())
    }
}

private fun Route.increaseUserApiCount(controller: UserController) = authenticate(FIREBASE_AUTH) {
    put("/updateApiCount") {
        val userId = call.principal<FirebaseUserPrincipal>()?.uid ?: return@put
        val response = controller.incUserApiReqCount(userId)
        call.respond(response.httpStatusCode, response.serialize())
    }
}

private fun Route.getAllUsers(controller: UserController) = authenticate(FIREBASE_AUTH) {
    get("/all") {
        val response = controller.getAllUsers()
        call.respond(response.httpStatusCode, response.serialize())
    }
}

private fun Route.deleteUser(controller: UserController) = authenticate(FIREBASE_AUTH) {
    delete("/delete") {
        val userId = call.principal<FirebaseUserPrincipal>()?.uid ?: return@delete
        val response = controller.deleteUser(userId)
        call.respond(response.httpStatusCode, response.serialize())
    }
}