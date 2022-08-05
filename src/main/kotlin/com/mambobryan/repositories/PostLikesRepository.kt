package com.mambobryan.repositories

import com.mambobryan.models.*
import com.mambobryan.utils.query
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.util.UUID

class PostLikesRepository {

    suspend fun getPostLikes() = query {
        Like.all().map { it.toLikeDto() }
    }

    suspend fun create(userId: UUID, postId: Int): Boolean? {

        var statement: InsertStatement<Number>? = null

        query {

            val condition = Op.build { Likes.postId eq postId and (Likes.userId eq userId) }

            val isLiked = Likes.select { condition }.firstOrNull() != null

            if (isLiked) return@query

            statement = Likes.insert {
                it[Likes.userId] = userId
                it[Likes.postId] = postId
            }
        }

        return statement?.resultedValues?.firstOrNull() != null

    }

    suspend fun delete(userId: UUID, postId: Int) = query {
        val condition = Op.build { Likes.postId eq postId and (Likes.userId eq userId) }
        val result = Likes.deleteWhere { condition }
        result == 0

    }

}