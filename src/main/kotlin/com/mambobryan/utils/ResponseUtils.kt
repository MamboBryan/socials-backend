package com.mambobryan.utils

import com.mambobryan.models.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import java.util.UUID

suspend fun ApplicationCall.defaultResponse(status: HttpStatusCode, message: String = "Error") {
    return this.respond(
        status = status, message = Response(success = status.isSuccess(), message = message, data = null)
    )
}

suspend fun <T> ApplicationCall.successWithData(status: HttpStatusCode, message: String = "Error", data: T) {
    return this.respond(
        status = status, message = Response(success = status.isSuccess(), message = message, data = data)
    )
}


fun ApplicationCall.getUserId(): UUID? {
    val principal = this.principal<JWTPrincipal>() ?: return null
    val idString = principal.payload.getClaim("id")?.asString() ?: return null
    return UUID.fromString(idString) ?: return null
}