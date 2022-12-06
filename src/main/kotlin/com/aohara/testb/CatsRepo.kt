package com.aohara.testb

import java.util.UUID

interface CatsRepo {
    operator fun get(id: UUID): Cat?
    fun listForOwner(ownerId: UUID): List<Cat>
    operator fun plusAssign(cat: Cat)
    operator fun minusAssign(id: UUID)
}