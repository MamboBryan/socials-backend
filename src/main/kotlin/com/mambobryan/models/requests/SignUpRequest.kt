package com.mambobryan.models.requests

data class SignUpRequest(
    val username: String,
    val email: String,
    val password: String
)