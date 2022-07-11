package com.mambobryan.plugins

import com.mambobryan.repositories.UsersRepository
import com.mambobryan.routes.authRoutes
import com.mambobryan.routes.postRoutes
import com.mambobryan.routes.usersRoutes
import com.mambobryan.utils.defaultResponse
import com.mambobryan.utils.successWithData
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureRouting(
    hashFunction: (s: String) -> String
) {

    val audience = environment.config.property("jwt.audience").getString()
    val issuer = environment.config.property("jwt.issuer").getString()

    routing {

        authRoutes(issuer = issuer, audience = audience, hashing = hashFunction)

        authenticate("auth-jwt") {

            usersRoutes()

            postRoutes()

            get("/") {

                val principal = call.principal<JWTPrincipal>()

                val userId = principal!!.payload.getClaim("id") ?: return@get call.defaultResponse(
                    status = HttpStatusCode.NotAcceptable, message = "I have no idea"
                )

                val user = UsersRepository().getUser(userId.asInt()) ?: return@get call.defaultResponse(
                    status = HttpStatusCode.NotFound, message = "No User Found!"
                )

                call.successWithData(status = HttpStatusCode.OK, message = "I found you!", data = user)
            }

        }
    }
}
