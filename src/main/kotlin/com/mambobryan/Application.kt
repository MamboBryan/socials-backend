package com.mambobryan

import io.ktor.server.application.*
import com.mambobryan.plugins.*
import com.mambobryan.utils.DatabaseUtils
import com.mambobryan.utils.JwtService
import com.mambobryan.utils.defaultResponse
import com.mambobryan.utils.hash
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    DatabaseUtils.create()

    val secret = System.getenv("SECRET_KEY")
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()

    val hashFunction: (String) -> String = { pass: String -> hash(secret = secret, password = pass) }

    install(Authentication) {
        jwt("auth-jwt") {
            realm = myRealm
            verifier(JwtService().getVerifier(audience = audience, issuer = issuer))
            validate { credential ->
                when (credential.payload.getClaim("id").asString() != "") {
                    true -> JWTPrincipal(credential.payload)
                    else -> null
                }
            }
            challenge { defaultScheme, realm ->
                call.defaultResponse(status = HttpStatusCode.Unauthorized, message = "Sign In to continue")
            }
        }
    }

    configureRouting(hashFunction)
    configureSerialization()

}
