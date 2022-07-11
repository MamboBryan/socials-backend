package com.mambobryan.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object PostLikes : IntIdTable() {

    val postId =
        reference("post_id", Posts.postId, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)
    val userId = reference("user_id", Users.id, onDelete = ReferenceOption.CASCADE, onUpdate = ReferenceOption.CASCADE)

}