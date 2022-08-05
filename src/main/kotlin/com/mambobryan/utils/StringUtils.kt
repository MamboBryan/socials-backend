package com.mambobryan.utils

import java.util.UUID

fun String?.asUUID(): UUID? {
    if (this.isNullOrBlank()) return null
    return UUID.fromString(this) ?: null
}

fun UUID?.asString(): String? = this?.toString()