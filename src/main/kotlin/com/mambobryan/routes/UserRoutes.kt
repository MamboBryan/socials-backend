package com.mambobryan.routes

import com.mambobryan.models.Response
import com.mambobryan.models.requests.UserIdRequest
import com.mambobryan.models.requests.UserRequest
import com.mambobryan.repositories.PostsRepository
import com.mambobryan.repositories.UsersRepository
import com.mambobryan.utils.asUUID
import com.mambobryan.utils.defaultResponse
import com.mambobryan.utils.getUserId
import com.mambobryan.utils.successWithData
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

fun Route.usersRoutes() {

    val repository = UsersRepository()
    val postsRepository = PostsRepository()

    route("users") {

        get {
            val users = repository.getUsers()
            when (users.isEmpty()) {
                true -> call.defaultResponse(message = "No User found", status = HttpStatusCode.OK)
                false -> call.successWithData(
                    status = HttpStatusCode.OK, message = "Users Found", data = users
                )
            }
        }

        put("update") {

            val userId = call.getUserId() ?: return@put call.defaultResponse(
                status = HttpStatusCode.NotAcceptable, message = "I have no idea"
            )

            val request = call.receive<UserRequest>()

            if (request.email.isNullOrBlank() && request.name.isNullOrBlank()) return@put call.defaultResponse(
                status = HttpStatusCode.BadRequest, "Email and Name cannot be empty"
            )

            val user = repository.update(id = userId, email = request.email, name = request.name)
                ?: return@put call.defaultResponse(
                    status = HttpStatusCode.NotFound, message = "User Not Found",
                )

            call.successWithData(
                status = HttpStatusCode.Created, message = "User updated successfully", data = user
            )

        }

        route("{id?}") {

            post {

                val currentUserId = call.getUserId() ?: return@post call.defaultResponse(
                    status = HttpStatusCode.NotAcceptable, message = "I have no idea"
                )

                val id = call.receive<UserIdRequest>().userId.asUUID() ?: return@post call.defaultResponse(
                    status = HttpStatusCode.BadRequest, message = "Missing Id",
                )

                val user = repository.getUser(id = id) ?: return@post call.defaultResponse(
                    status = HttpStatusCode.NotFound, message = "User Not Found",
                )

                call.successWithData(status = HttpStatusCode.OK, message = "success", data = user)

            }

            post("posts") {

//                val id = call.receive<UserIdRequest>().userId.asUUID() ?: return@post call.defaultResponse(
//                    status = HttpStatusCode.BadRequest, message = "Missing Id",
//                )

                val userId = call.getUserId() ?: return@post call.defaultResponse(
                    status = HttpStatusCode.NotAcceptable, message = "I have no idea"
                )

                val posts = postsRepository.getUserPosts(currentUserId = userId, userId = userId)

                return@post when (posts.isEmpty()) {
                    true -> call.defaultResponse(message = "User has no post", status = HttpStatusCode.OK)
                    false -> call.respond(
                        Response(
                            success = true, message = "Posts Found", data = posts
                        )
                    )
                }

            }

            delete {

                val id = call.receive<UserIdRequest>().userId.asUUID() ?: return@delete call.defaultResponse(
                    status = HttpStatusCode.BadRequest, message = "Missing Id",
                )

                return@delete when (repository.delete(id)) {
                    true -> call.defaultResponse(
                        status = HttpStatusCode.BadRequest, message = "Failed deleting user",
                    )

                    false -> call.defaultResponse(
                        status = HttpStatusCode.OK, message = "User deleted",
                    )
                }
            }

        }
    }

}