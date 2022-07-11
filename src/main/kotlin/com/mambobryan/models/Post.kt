package com.mambobryan.models

data class Post(
    val postId: Long, val content: String, val userId: Int, val likeId: Int?
)
