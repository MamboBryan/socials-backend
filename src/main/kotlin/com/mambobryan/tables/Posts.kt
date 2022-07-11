package com.mambobryan.tables

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

object Posts : Table() {

    val postId = long("post_id").autoIncrement().uniqueIndex()
    val userId = reference("user_id", Users.id, onDelete = ReferenceOption.CASCADE)
    val content = varchar("post_content", 128)

    override val primaryKey: PrimaryKey = PrimaryKey(postId)
}