package com.mambobryan.repositories

class PostLikesRepository {



//    suspend fun getPostLikes() = query {
//        PostLikes.selectAll().map { it.toPostLike() }
//    }
//
//    suspend fun create(userId: Int, postId: Int): PostLike? {
//
//        var statement: InsertStatement<Number>? = null
//
//        query {
//
//            val condition = Op.build { PostLikes.postId eq postId.toLong() and (PostLikes.userId eq userId) }
//
//            val isNotLiked = PostLikes.select { condition }.count() > 0L
//
////            if (isNotLiked)
//            statement = PostLikes.insert {
//                it[PostLikes.userId] = userId
//                it[PostLikes.postId] = postId.toLong()
//            }
//        }
//
//        return statement?.resultedValues?.firstOrNull().toPostLike()
//
//    }
//
//    suspend fun delete(userId: Int, postId: Int) = query {
//        val condition = Op.build { PostLikes.postId eq postId.toLong() and (PostLikes.userId eq userId) }
//        val result = PostLikes.deleteWhere { condition }
//        result == 0
//
//    }

}