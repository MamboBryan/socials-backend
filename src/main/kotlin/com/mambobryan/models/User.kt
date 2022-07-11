package com.mambobryan.models

import com.mambobryan.utils.Exclude

data class User(
    val userId: Int,
    val userName: String,
    val email: String,
    @Exclude val hash: String
)
