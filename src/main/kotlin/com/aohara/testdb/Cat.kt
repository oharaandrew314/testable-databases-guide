package com.aohara.testdb

import java.util.UUID

data class Cat(
    val id: UUID,
    val name: String,
    val ownerId: UUID
)