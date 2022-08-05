package com.mambobryan.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.javatime.datetime

object Posts : IntIdTable() {

    val createdAt = datetime("post_created_at")
    val updatedAt = datetime("post_updated_at")
    val content = text("post_content")

    val userId = reference("user_id", Users)

}

class PostEntity(mId: EntityID<Int>) : IntEntity(mId) {

    companion object : IntEntityClass<PostEntity>(Posts)

    var createdAt by Posts.createdAt
    var updatedAt by Posts.updatedAt
    var content by Posts.content

    var user by User referencedOn Posts.userId
    val likes by Like referrersOn Likes.postId

}

data class PostDTO(
    val id: Int,
    val createdAt: String,
    val updatedAt: String,
    val content: String,
    val user: UserDTO?,
    val like: LikeDTO? = null
)

data class PostLikeDTO(
    val id: Int,
    val createdAt: String,
    val updatedAt: String,
    val content: String,
    val user: UserDTO?,
    val likes: List<LikeDTO?>,
    val count: Long?
)

data class UserPost(
    val createdAt: String,
    val updatedAt: String,
    val content: String,
)

fun PostEntity?.toPostDto(): PostDTO? {
    if (this == null) return null
    return PostDTO(
        id = this.id.value,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString(),
        content = content,
        user = this.user.toDto(),
    )
}

fun PostEntity?.toPostLikeDto(): PostLikeDTO? {
    if (this == null) return null
    return PostLikeDTO(
        id = this.id.value,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString(),
        content = content,
        user = this.user.toDto(),
        likes = this.likes.map { it.toLikeDto() },
        count = this.likes.count()
    )
}

fun PostEntity?.toUserPost(): UserPost? {
    if (this == null) return null
    return UserPost(
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString(),
        content = this.content,
    )
}

internal fun ResultRow?.toPost(): PostDTO? {
    if (this == null) return null
    return PostDTO(
        id = this[Posts.id].value,
        createdAt = this[Posts.createdAt].toString(),
        updatedAt = this[Posts.updatedAt].toString(),
        content = this[Posts.content],
        user = null,
        like = null
    )
}


