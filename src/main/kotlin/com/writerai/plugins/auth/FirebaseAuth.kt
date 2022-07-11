package com.writerai.plugins.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import com.writerai.data.models.response.Response
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*

data class FirebaseUserPrincipal(val uid: String, val emailAddress: String, val image: String) :
    Principal

class FirebaseAuthenticationProvider(private val config: Configuration) : AuthenticationProvider(config) {

    private val authHeader: (ApplicationCall) -> String? = config.authHeader
    private val authenticationFunction = config.authenticationFunction
    private var challengeFunction: suspend (ApplicationCall) -> Unit = config.challengeFunction

    class Configuration(configName: String) : AuthenticationProvider.Config(configName) {

        internal var authenticationFunction: AuthenticationFunction<FirebaseToken> = {
            throw NotImplementedError(
                "JWT auth validate function is not specified. Use jwt { validate { ... } } to fix."
            )
        }

        internal var challengeFunction: suspend (ApplicationCall) -> Unit = { call ->
            call.respond("Not Authorized")
        }

        internal var authHeader: (ApplicationCall) -> String? =
            { call -> call.request.header("Authorization")?.substringAfter(" ") }

        fun build() = FirebaseAuthenticationProvider(this)

        fun validate(validate: suspend ApplicationCall.(FirebaseToken) -> Principal?) {
            authenticationFunction = validate
        }

        fun challenge(block: suspend (ApplicationCall) -> Unit) {
            challengeFunction = block
        }
    }

    override suspend fun onAuthenticate(context: AuthenticationContext) {
        val call = context.call
        val token = authHeader(call)
        if (token == null) {
            context.challenge(
                AuthenticationFailedCause.NoCredentials,
                AuthenticationFailedCause.NoCredentials
            ) { _, _ ->
                val er = Response.Error<Unit>("Please pass token", status = 401)
                call.respond(
                    er.httpStatusCode,
                    er.serialize()
                )
            }
            return
        }
        try {
            val fToken = FirebaseAuth.getInstance().verifyIdToken(token)
            val principal = FirebaseUserPrincipal(
                uid = fToken.uid,
                emailAddress = fToken.email,
                image = fToken.picture ?: "",
            )
            context.principal(principal)
        } catch (cause: Throwable) {
            val er = Response.Error<Unit>(cause.message.toString(), status = 401)
            call.respond(er.httpStatusCode, er.serialize())
        }
    }
}

fun AuthenticationConfig.firebase(
    configName: String = "firebaseAuth",
    configure: FirebaseAuthenticationProvider.Configuration.() -> Unit
) {
    val provider = FirebaseAuthenticationProvider.Configuration(configName).apply(configure).build()
    register(provider)
}