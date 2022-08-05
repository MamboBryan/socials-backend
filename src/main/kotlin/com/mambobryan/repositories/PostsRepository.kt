package com.mambobryan.repositories

import com.mambobryan.models.*
import com.mambobryan.utils.query
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement
import java.time.LocalDateTime
import java.util.*

class PostsRepository {

    suspend fun getPosts(userId: UUID) = query {

//        val condition = Op.build {
//            Likes.postId eq Posts.id and (Likes.userId eq userId)
//        }
//
//        val likesCount = Likes.postId.count().alias("likesCount")
//        val likeQuery = Likes.slice(likesCount).select { condition }.orderBy(Posts.id).alias("likesQuery")
//
//        Posts.join(Users, JoinType.INNER, additionalConstraint = { Posts.userId eq Users.id })
//            .join(
//                otherTable = Likes,
//                joinType = JoinType.LEFT,
//                additionalConstraint = { condition }
//            )
//            .slice(
//                Likes.postId.count().alias("likes"),
//                *Posts.fields.toTypedArray(),
//                *Users.fields.toTypedArray(),
//                *Likes.fields.toTypedArray()
//            )
//            .selectAll()
//            .groupBy(Posts.id, Users.id, Likes.id)
//            .map {
//                val map = mutableMapOf<String, String>()
//                for ((key, value) in it.fieldIndex) {
//                    map[key.toString()] = value.toString()
//                }
//                map
//            }


        Posts.innerJoin(Users).leftJoin(Likes).selectAll().map {
            mapOf(
                "post" to it.toPost(),
                "user" to it.toUser(),
//                    "like" to it.toLike()
            )
        }

//        PostEntity.all().sortedByDescending { Posts.updatedAt }.map {
//            it.toPostLikeDto()
//        }


    }

    suspend fun getUserPosts(currentUserId: UUID, userId: UUID) = query {

//        val condition = Op.build {
//            PostLikes.postId eq Posts.postId and (PostLikes.userId eq currentUserId)
//        }
//
//        Posts.join(Users, JoinType.INNER, additionalConstraint = { Posts.userId eq userId })
//            .join(
//                otherTable = PostLikes,
//                joinType = JoinType.LEFT,
//                additionalConstraint = { condition }
//            )
//            .selectAll()
//            .limit(20, 20)
//            .map { it.toCompletePost() }

        PostEntity.find { Posts.userId eq userId }.sortedBy { Posts.updatedAt }.map { it.toUserPost() }
//            .map { "content" to it.content }
//        Post.find { Posts.userId eq userId }
//            .sortedBy { Posts.updatedAt }
//            .map { it.toPostDto() }

    }

    suspend fun getPost(id: Int) = query {
//        Posts.select { Posts.postId eq id.toLong() }.map { it.toPost() }.singleOrNull()
        PostEntity.findById(id).toPostDto()
    }

    suspend fun create(userId: UUID, content: String): PostDTO? {

        var statement: InsertStatement<Number>? = null

        query {
            statement = Posts.insert {
                it[Posts.createdAt] = LocalDateTime.now()
                it[Posts.updatedAt] = LocalDateTime.now()
                it[Posts.content] = content
                it[Posts.userId] = userId
            }
        }

        return statement?.resultedValues?.get(0).toPost()


    }

    suspend fun update(postId: Int, userId: UUID, content: String?) = query {

//        val condition = Op.build { Posts.postId eq postId.toLong() and (Posts.userId eq userId) }
//
//        Posts.update({ condition }) {
//            if (content != null) it[Posts.content] = Posts.content
//        }
//
//        Posts.select { Posts.postId eq postId.toLong() }.map { it.toPost() }.singleOrNull()

        val postEntity =
            PostEntity.find { Posts.id eq postId and (Posts.userId eq userId) }.singleOrNull() ?: return@query null

        if (content != null) postEntity.content = content

        postEntity.toPostDto()

    }

    suspend fun delete(userId: UUID, postId: Int) = query {

        val postEntity =
            PostEntity.find { Posts.id eq postId and (Posts.userId eq userId) }.singleOrNull() ?: return@query false

        postEntity.delete()

        true

//        val result = Posts.deleteWhere { Posts.postId eq postId.toLong() }
//        result == 0
    }

}