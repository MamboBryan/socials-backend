package com.mambobryan.models

import com.google.gson.annotations.SerializedName
import com.mambobryan.utils.Exclude
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object Users : UUIDTable(name = "users", columnName = "user_id") {

    val username = varchar("user_name", 255)
    val email = varchar("user_email", 255).uniqueIndex()
    val hash = text("user_hash")

}

class User(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<User>(Users)

    var userName by Users.username
    var email by Users.email

    @Exclude
    var hash by Users.hash

}

data class UserDTO(
    val username: String?,
    val email: String?,
    @Exclude val hash: String?
)

fun User?.idAsString() = this?.id?.value.toString()

fun User?.toDto(): UserDTO? {
    if (this == null) return null
    return UserDTO(
        this.userName,
        this.email,
        this.hash
    )
}

