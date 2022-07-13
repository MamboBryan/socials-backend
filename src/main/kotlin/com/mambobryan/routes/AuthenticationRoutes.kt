package com.mambobryan.routes

import com.mambobryan.models.requests.SignInRequest
import com.mambobryan.models.requests.SignUpRequest
import com.mambobryan.models.toDto
import com.mambobryan.repositories.UsersRepository
import com.mambobryan.utils.JwtService
import com.mambobryan.utils.defaultResponse
import com.mambobryan.utils.successWithData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.authRoutes(
    issuer: String, audience: String, hashing: (String) -> String
) {

    val repository = UsersRepository()

    route("auth") {

        post("/signin") {

            val request = call.receive<SignInRequest>()

            if (request.email.isNullOrBlank() or request.password.isNullOrBlank()) return@post call.defaultResponse(
                status = HttpStatusCode.BadRequest, message = "Email or Password cannot be blank"
            )

            val user = repository.getUserByEmail(request.email!!) ?: return@post call.defaultResponse(
                status = HttpStatusCode.Unauthorized, message = "Failed Signing In"
            )

            return@post try {

                val hash = hashing(request.password!!)

                when (user.hash == hash) {
                    true -> {
                        val token = JwtService().generateToken(issuer = issuer, audience = audience, user = user)

                        val data = mapOf(
                            "token" to token, "user" to user.toDto()
                        )

                        call.successWithData(
                            status = HttpStatusCode.OK, message = "Signed Up successfully", data = data
                        )
                    }
                    false -> call.defaultResponse(
                        status = HttpStatusCode.NotAcceptable, message = "Invalid Credentials"
                    )
                }

            } catch (e: Exception) {
                call.defaultResponse(
                    status = HttpStatusCode.NotAcceptable, message = "Failed Signing in"
                )
            }
        }

        post("/signup") {

            val request = call.receive<SignUpRequest>()

            if (request.username.isBlank() or request.email.isBlank() or request.password.isBlank()) return@post call.defaultResponse(
                status = HttpStatusCode.BadRequest, message = "Invalid Sign Up details"
            )

            val hash = hashing(request.password)

            val user = repository.create(email = request.email, name = request.username, hash = hash)

            if (user.id.value.toString().isBlank()) return@post call.defaultResponse(
                status = HttpStatusCode.Unauthorized, message = "Failed signing up"
            )

            val token = JwtService().generateToken(issuer = issuer, audience = audience, user = user).also {
                println("TOKEN -> $it")
            }

            val data = mapOf(
                "token" to token, "user" to user.toDto()
            )

//            return@post call.defaultResponse(
//                status = HttpStatusCode.OK, message = "Success"
//            )

            return@post call.successWithData(
                status = HttpStatusCode.Created, message = "Signed Up successfully", data = data
            )

        }

    }

}