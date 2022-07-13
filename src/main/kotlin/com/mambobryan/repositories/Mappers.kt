package com.mambobryan.repositories

//internal fun ResultRow?.toUser(): User? {
//    if (this == null) return null
//    return User(
//        userId = this[Users.id], userName = this[Users.name], email = this[Users.email], hash = this[Users.hash]
//    )
//}
//
//internal fun ResultRow?.toCompletePost(): CompletePost? {
//    if (this == null) return null
//
//    val post = this.toPost()
//    val user = this.toUser()
//    val like = this.toPostLike()
//
//    return CompletePost(
//        postId = post?.postId,
//        postContent = post?.content,
//        likeId = like?.likeId,
//        user = user,
//        likes = 0
//    )
//}
//
//internal fun Query?.toPost(): Post? {
//    return this?.map { it.toPost() }?.firstOrNull()
//}
//
//internal fun ResultRow?.toPost(): Post? {
//    if (this == null) return null
//    return Post(
//        postId = this[Posts.postId], userId = this[Posts.userId], content = this[Posts.content]
//    )
//}
//
//internal fun ResultRow?.toPostLike(): PostLike? {
//    if (this == null) return null
//    return try {
//        PostLike(
//            likeId = this[PostLikes.id].value, postId = this[PostLikes.postId], userId = this[PostLikes.userId]
//        )
//    } catch (e: Exception) {
//        null
//    }
//}