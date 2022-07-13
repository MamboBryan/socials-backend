package com.mambobryan.models.responses

import com.mambobryan.models.User

data class CompletePost(
    val postId: Long?,
    val postContent: String?,
    val likeId: Int?,
    val user: User?,
    val likes: Long
)