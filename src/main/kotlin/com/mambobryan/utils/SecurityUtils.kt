package com.mambobryan.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.mambobryan.models.User
import io.ktor.util.*
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

fun hash(secret: String, password: String): String {
    val key = hex(secret)
    val hmacKey = SecretKeySpec(key, "HmacSHA1")
    val hmac = Mac.getInstance("HmacSHA1")
    hmac.init(hmacKey)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}

class JwtService {

    private val secret = System.getenv("SECRET_KEY")

    private val algorithm = Algorithm.HMAC512(secret)

    private fun expiresAt() = Date(System.currentTimeMillis() + 3_600_000 * 24)

    fun getVerifier(audience: String, issuer: String) = JWT
        .require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()

    fun generateToken(issuer: String, audience: String, user: User): String = JWT
        .create()
        .withAudience(audience)
        .withIssuer(issuer)
        .withExpiresAt(expiresAt())
        .withClaim("id", user.userId)
        .sign(algorithm)

}