package com.mambobryan.repositories

import com.mambobryan.models.User
import com.mambobryan.models.Users
import com.mambobryan.utils.query
import java.util.*

class UsersRepository {

    suspend fun getUsers(): List<User?> = query {
//        User.s().orderBy(Users.id, SortOrder.DESC)
        User.all().toList()
    }

    suspend fun getUser(id: UUID): User? = query {
//        Users.select { Users.id eq id }.map { it.toUser() }.singleOrNull()
        User.findById(id = id)
    }

    suspend fun getUserByEmail(email: String): User? = query {
//        Users.select { Users.email eq email }.map { it.toUser() }.singleOrNull()
        User.find { Users.email eq email }.singleOrNull()
    }

    suspend fun create(email: String, name: String, hash: String): User = query{

        val user = User.new {
            this.email = email
            this.userName = name
            this.hash = hash
        }

        println("USER IS NULL -> ${user == null}")
        println("USER -> ${user.id} | ${user.userName} | ${user.email}")

        user

//        var statement: InsertStatement<Number>? = null
//
//        query {
//            statement = Users.insert {
//                it[Users.email] = email
//                it[Users.name] = name
//                it[Users.hash] = hash
//            }
//        }
//
//        return statement?.resultedValues?.get(0).toUser()

    }

    suspend fun update(id: UUID, email: String?, name: String?): User? = query {

        val user: User? = User.findById(id)

        if (user != null) {
            if (email != null) user.email = email
            if (name != null) user.userName = name
        }
        user

//        Users.update({ Users.id eq id }) {
//            if (email != null) it[Users.email] = email
//            if (name != null) it[Users.name] = name
//        }
//
//        Users.select(Users.id eq id).map { it.toUser() }.singleOrNull()

    }

    suspend fun delete(id: UUID) = query {

        val user: User = User.findById(id) ?: return@query false

        user.delete()

        true

//        val result = Users.deleteWhere { Users.id eq id }
//        result == 0
    }

}