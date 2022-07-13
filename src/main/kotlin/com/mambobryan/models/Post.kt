package com.mambobryan.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Posts : IntIdTable(){

    val createdAt = datetime("post_created_at")
    val updatedAt = datetime("post_updated_at")
    val content = text("post_content")
    val user = reference("user_id", Users)

}

class Post (id: EntityID<Int>) : IntEntity(id) {

    companion object : IntEntityClass<Post>(Posts)

    var createdAt by Posts.createdAt
    var updatedAt by Posts.updatedAt
    var content by Posts.content

    var user by User referencedOn Users.id

}

