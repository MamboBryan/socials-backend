package com.mambobryan.tables

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Users : Table() {

    val id: Column<Int> = integer("user_id").autoIncrement().uniqueIndex()
    val email = varchar("user_email", 50).uniqueIndex()
    val name = varchar("user_name", 50)
    val hash = varchar("user_hash", 128)

    override val primaryKey: PrimaryKey = PrimaryKey(email)

}