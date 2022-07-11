package com.writerai.plugins

import com.writerai.data.models.response.Response
import com.writerai.data.repo.UserRepo
import com.writerai.plugins.auth.FirebaseUserPrincipal
import com.writerai.plugins.auth.firebase
import com.writerai.utils.FIREBASE_AUTH
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

fun Application.configureAuthentication() {
    val userRepo by inject<UserRepo>()
    install(Authentication) {
        firebase(FIREBASE_AUTH) {
            validate { credential ->
                println(credential.uid)
                if(userRepo.getUserById(credential.uid) is Response.Error){
                    challenge {respond(HttpStatusCode.Unauthorized, Response.Error<Unit>("User does not exist").serialize()) }
                    return@validate null
                }
                FirebaseUserPrincipal(
                    image = credential.picture,
                    emailAddress = credential.email,
                    uid = credential.uid
                )
            }
        }
    }
}