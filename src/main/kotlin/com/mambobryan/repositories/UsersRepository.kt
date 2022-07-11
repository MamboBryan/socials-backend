package com.mambobryan.repositories

import com.mambobryan.models.User
import com.mambobryan.tables.Users
import com.mambobryan.utils.query
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement

class UsersRepository {

    suspend fun getUsers(): List<User?> = query {
        Users.selectAll().map { it.toUser() }
    }

    suspend fun getUser(id: Int): User? = query {
        Users.select { Users.id eq id }.map { it.toUser() }.singleOrNull()
    }

    suspend fun getUserByEmail(email: String): User? = query {
        Users.select { Users.email eq email }.map { it.toUser() }.singleOrNull()
    }

    suspend fun create(email: String, name: String, hash: String): User? {

        var statement: InsertStatement<Number>? = null

        query {
            statement = Users.insert {
                it[Users.email] = email
                it[Users.name] = name
                it[Users.hash] = hash
            }
        }

        return statement?.resultedValues?.get(0).toUser()

    }

    suspend fun update(id: Int, email: String?, name: String?): User? = query {

        Users.update({ Users.id eq id }) {
            if (email != null) it[Users.email] = email
            if (name != null) it[Users.name] = name
        }

        Users.select(Users.id eq id).map { it.toUser() }.singleOrNull()

    }

    suspend fun delete(id: Int) = query {
        val result = Users.deleteWhere { Users.id eq id }
        result == 0
    }

}