package com.mambobryan.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object Likes : IntIdTable() {

    val postId = reference("post", Posts)
    val userId = reference("user", Users)

}

class Like(id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<Like>(Likes)

    var postId by Posts.id
    var userId by Users.id

}

