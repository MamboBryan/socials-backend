package com.mambobryan.routes

import com.mambobryan.models.requests.PostRequest
import com.mambobryan.repositories.PostLikesRepository
import com.mambobryan.repositories.PostsRepository
import com.mambobryan.utils.defaultResponse
import com.mambobryan.utils.getUserId
import com.mambobryan.utils.successWithData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Route.postRoutes() {

    val repository = PostsRepository()
    val likesRepository = PostLikesRepository()

    route("posts") {

        get {

            val userId = call.getUserId() ?: return@get call.defaultResponse(
                status = HttpStatusCode.NotAcceptable, message = "Authentication Failed!"
            )

            val posts = repository.getPosts(userId = userId)

            when (posts.isEmpty()) {
                true -> call.defaultResponse(message = "No Post found", status = HttpStatusCode.OK)
                false -> call.successWithData(
                    status = HttpStatusCode.OK, message = "Posts Found", data = posts
                )
            }
        }

        post {

            val request = call.receive<PostRequest>()
            val userId = call.getUserId() ?: return@post call.defaultResponse(
                status = HttpStatusCode.NotAcceptable, message = "Authentication Failed!"
            )

            if (request.content.isNullOrBlank()) return@post call.defaultResponse(
                status = HttpStatusCode.BadRequest, "User Id and Content cannot be empty"
            )

            val post = repository.create(
                userId = userId, content = request.content
            ) ?: return@post call.defaultResponse(
                status = HttpStatusCode.Conflict, message = "Failed creating post"
            )

            call.successWithData(
                status = HttpStatusCode.Created, message = "Post created successfully", data = post
            )

        }

        route("{id?}") {

            get {
                val id = call.parameters["id"] ?: return@get call.defaultResponse(
                    status = HttpStatusCode.BadRequest, message = "Missing Id",
                )

                val post = repository.getPost(id = id.toInt()) ?: return@get call.defaultResponse(
                    status = HttpStatusCode.NotFound, message = "Post Not Found",
                )

                call.successWithData(status = HttpStatusCode.OK, message = "success", data = post)

            }

            put {
                val id = call.parameters["id"] ?: return@put call.defaultResponse(
                    status = HttpStatusCode.BadRequest, message = "Missing Id",
                )

                val request = call.receive<PostRequest>()

                val userId = call.getUserId() ?: return@put call.defaultResponse(
                    status = HttpStatusCode.NotAcceptable, message = "Authentication Failed!"
                )

                if (request.content.isNullOrBlank()) return@put call.defaultResponse(
                    status = HttpStatusCode.BadRequest, "Content cannot be blank"
                )

                val user = repository.update(postId = id.toInt(), userId = userId, content = request.content)
                    ?: return@put call.defaultResponse(
                        status = HttpStatusCode.NotFound, message = "User Not Found",
                    )

                call.successWithData(
                    status = HttpStatusCode.Created, message = "User updated successfully", data = user
                )
            }

            delete {

                val id = call.parameters["id"] ?: return@delete call.defaultResponse(
                    status = HttpStatusCode.BadRequest, message = "Missing Id",
                )

                val userId = call.getUserId() ?: return@delete call.defaultResponse(
                    status = HttpStatusCode.NotAcceptable, message = "Authentication Failed!"
                )

                return@delete when (repository.delete(userId = userId, postId = id.toInt())) {
                    true -> call.defaultResponse(
                        status = HttpStatusCode.BadRequest, message = "Failed deleting post",
                    )
                    false -> call.defaultResponse(
                        status = HttpStatusCode.OK, message = "Post deleted",
                    )
                }
            }

            route("likes") {

//                get {
//                    val likes = likesRepository.getPostLikes()
//                    when (likes.isEmpty()) {
//                        true -> call.defaultResponse(message = "No Like found", status = HttpStatusCode.OK)
//                        false -> call.successWithData(
//                            status = HttpStatusCode.OK, message = "Likes Found", data = likes
//                        )
//                    }
//                }

//                post {
//
//                    val id = call.parameters["id"] ?: return@post call.defaultResponse(
//                        status = HttpStatusCode.BadRequest, message = "Missing Id",
//                    )
//
//                    val userId = call.getUserId() ?: return@post call.defaultResponse(
//                        status = HttpStatusCode.NotAcceptable, message = "Authentication Failed!"
//                    )
//
//                    val like = likesRepository.create(userId = userId, postId = id.toInt())
//                        ?: return@post call.defaultResponse(
//                            status = HttpStatusCode.BadRequest, message = "Failed liking post"
//                        )
//
//                    call.successWithData(
//                        status = HttpStatusCode.Created, message = "Post liked successfully", data = like
//                    )
//
//                }

//                delete {
//
//                    val id = call.parameters["id"] ?: return@delete call.defaultResponse(
//                        status = HttpStatusCode.BadRequest, message = "Missing Id",
//                    )
//
//                    val userId = call.getUserId() ?: return@delete call.defaultResponse(
//                        status = HttpStatusCode.NotAcceptable, message = "Authentication Failed!"
//                    )
//
//                    return@delete when (likesRepository.delete(userId = userId, postId = id.toInt())) {
//                        true -> call.defaultResponse(
//                            status = HttpStatusCode.BadRequest, message = "Failed liking post",
//                        )
//                        false -> call.defaultResponse(
//                            status = HttpStatusCode.OK, message = "Post unliked successfully",
//                        )
//                    }
//                }

            }

        }

    }


}