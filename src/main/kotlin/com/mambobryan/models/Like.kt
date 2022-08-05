package com.mambobryan.models

import com.mambobryan.utils.asString
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import java.util.*

object Likes : UUIDTable() {

    val postId = reference("post_id", Posts)
    val userId = reference("user_id", Users)

}

internal fun ResultRow?.toLike(): LikeDTO? {
    if (this == null) return null
    return LikeDTO(
        likeId = this[Likes.id].value.asString(),
        user = null,
        post = null
    )
}

class Like(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<Like>(Likes)

    var user by User referencedOn Likes.userId
    var post by PostEntity referencedOn Likes.postId

}

object PostLikes : Table() {

    //    val post = reference("post", Posts)
    val like = reference("likes", Likes)

    override val primaryKey: PrimaryKey = PrimaryKey(like)

}

data class LikeDTO(
    val likeId: String?,
    val user: UserDTO?,
    val post: PostDTO?
)

fun Like?.toLikeDto(): LikeDTO? {
    if (this == null) return null
    return LikeDTO(
        likeId = this.id.toString(),
        user = this.user.toDto(),
        post = this.post.toPostDto()
    )
}
