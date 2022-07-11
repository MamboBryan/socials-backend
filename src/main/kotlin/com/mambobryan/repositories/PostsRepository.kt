package com.mambobryan.repositories

import com.mambobryan.models.Post
import com.mambobryan.tables.PostLikes
import com.mambobryan.tables.Posts
import com.mambobryan.tables.Users
import com.mambobryan.utils.query
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.statements.InsertStatement

class PostsRepository {

    suspend fun getPosts(userId: Int) = query {

        val condition = Op.build {
            PostLikes.postId eq Posts.postId and (PostLikes.userId eq userId)
        }

       Posts.join(Users, JoinType.INNER, additionalConstraint = { Posts.userId eq Users.id })
            .join(
                otherTable = PostLikes,
                joinType = JoinType.LEFT,
                additionalConstraint = { condition }
            )
            .selectAll()
            .limit(20, 20)
            .map { it.toCompletePost() }

    }

    suspend fun getUserPosts(currentUserId: Int, userId: Int) = query {

        val condition = Op.build {
            PostLikes.postId eq Posts.postId and (PostLikes.userId eq currentUserId)
        }

        Posts.join(Users, JoinType.INNER, additionalConstraint = { Posts.userId eq userId })
            .join(
                otherTable = PostLikes,
                joinType = JoinType.LEFT,
                additionalConstraint = { condition }
            )
            .selectAll()
            .limit(20, 20)
            .map { it.toCompletePost() }

    }

    suspend fun getPost(id: Int) = query {
        Posts.select { Posts.postId eq id.toLong() }.map { it.toPost() }.singleOrNull()
    }

    suspend fun create(userId: Int, content: String): Post? {

        var statement: InsertStatement<Number>? = null

        query {
            statement = Posts.insert {
                it[Posts.userId] = userId
                it[Posts.content] = content
            }
        }

        return statement?.resultedValues?.get(0).toPost()

    }

    suspend fun update(postId: Int, userId: Int, content: String?) = query {

        val condition = Op.build { Posts.postId eq postId.toLong() and (Posts.userId eq userId) }

        Posts.update({ condition }) {
            if (content != null) it[Posts.content] = Posts.content
        }

        Posts.select { Posts.postId eq postId.toLong() }.map { it.toPost() }.singleOrNull()

    }

    suspend fun delete(postId: Int) = query {
        val result = Posts.deleteWhere { Posts.postId eq postId.toLong() }
        result == 0
    }

}