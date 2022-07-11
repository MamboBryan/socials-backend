package com.mambobryan.models

data class Response<T>(
    val success: Boolean, val message: String, val data: T
)