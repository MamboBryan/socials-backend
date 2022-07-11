package com.mambobryan.utils

import com.mambobryan.tables.PostLikes
import com.mambobryan.tables.Posts
import com.mambobryan.tables.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.transactionScope

private fun hikariConfiguration(): HikariDataSource {
    val config = HikariConfig()

    config.apply {
        driverClassName = System.getenv("JDBC_DRIVER")
        jdbcUrl = System.getenv("JDBC_DATABASE_URL")
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
    }

    config.validate()
    return HikariDataSource(config)
}

suspend fun <T> query(block: () -> T): T = withContext(Dispatchers.IO) {
    transaction {
        block.invoke()
    }
}

object DatabaseUtils {

    fun create() {

        Database.connect(hikariConfiguration())

        transactionScope {
            SchemaUtils.createDatabase("socials")
        }

        transaction {
//            SchemaUtils.drop(Users, Posts, PostLikes)
            SchemaUtils.create(Users, Posts, PostLikes)
        }

    }
}
