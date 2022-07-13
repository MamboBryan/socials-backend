package com.mambobryan.repositories

import com.mambobryan.models.Post
import com.mambobryan.models.Posts
import com.mambobryan.models.User
import com.mambobryan.utils.query
import org.jetbrains.exposed.sql.and
import java.time.LocalDateTime
import java.util.*

class PostsRepository {

    suspend fun getPosts(userId: UUID) = query {

//        val condition = Op.build {
//            PostLikes.postId eq Posts.postId and (PostLikes.userId eq userId)
//        }

//        val likesCount: ExpressionAlias<Long> = PostLikes.postId.count().alias("likesCount")
//        val likeQuery = PostLikes.slice(likesCount).select { condition }.orderBy(Posts.postId).alias("likesQuery")

//        Posts.join(Users, JoinType.INNER, additionalConstraint = { Posts.userId eq Users.id })
//            .join(
//                otherTable = PostLikes,
//                joinType = JoinType.LEFT,
//                additionalConstraint = { condition }
//            )
//            .slice(
//                PostLikes.postId.count().alias("likes"),
//                *Posts.fields.toTypedArray(),
//                *Users.fields.toTypedArray(),
//                *PostLikes.fields.toTypedArray()
//            )
//            .selectAll()
//            .groupBy(Posts.postId)
//            .map { it.toCompletePost() }


        Post.all().sortedBy { Posts.updatedAt }


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

        Post.find { Posts.user eq userId }.sortedBy { Posts.updatedAt }

    }

    suspend fun getPost(id: Int) = query {
//        Posts.select { Posts.postId eq id.toLong() }.map { it.toPost() }.singleOrNull()
        Post.findById(id)
    }

    suspend fun create(userId: UUID, content: String): Post? = query {

//        var statement: InsertStatement<Number>? = null
//
//        query {
//            statement = Posts.insert {
//                it[Posts.userId] = userId
//                it[Posts.content] = content
//            }
//        }
//
//        return statement?.resultedValues?.get(0).toPost()

        val post = Post.new {
            this.createdAt = LocalDateTime.now()
            this.updatedAt = LocalDateTime.now()
            this.content = content
//            this.user = Select
        }

        post

    }

    suspend fun update(postId: Int, userId: UUID, content: String?) = query {

//        val condition = Op.build { Posts.postId eq postId.toLong() and (Posts.userId eq userId) }
//
//        Posts.update({ condition }) {
//            if (content != null) it[Posts.content] = Posts.content
//        }
//
//        Posts.select { Posts.postId eq postId.toLong() }.map { it.toPost() }.singleOrNull()

        val post = Post.find { Posts.id eq postId and (Posts.user eq userId) }.singleOrNull() ?: return@query null

        if (content != null) post.content = content

        post

    }

    suspend fun delete(userId: UUID, postId: Int) = query {

        val post = Post.find { Posts.id eq postId and (Posts.user eq userId) }.singleOrNull() ?: return@query false

        post.delete()

        true

//        val result = Posts.deleteWhere { Posts.postId eq postId.toLong() }
//        result == 0
    }

}