package com.mambobryan.utils

import java.util.UUID

fun String?.asUUID(): UUID? = UUID.fromString(this) ?: null
fun UUID?.asString(): String? = this?.toString()