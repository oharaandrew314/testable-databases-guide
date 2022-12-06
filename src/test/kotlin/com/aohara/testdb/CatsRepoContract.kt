package com.aohara.testdb

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.UUID

private val ownerId = UUID.randomUUID()

private val kratos = Cat(
    id = UUID.randomUUID(),
    ownerId = ownerId,
    name = "Kratos"
)

private val athena = Cat(
    id = UUID.randomUUID(),
    ownerId = ownerId,
    name = "Athena"
)

abstract class CatsRepoContract(private val repo: CatsRepo) {

    init {
        repo += kratos
        repo += athena
    }

    @Test
    fun `get missing`() {
        repo[UUID.randomUUID()] shouldBe null
    }

    @Test
    fun `get single`() {
        repo[kratos.id] shouldBe kratos
    }

    @Test
    fun `list for missing owner`() {
        repo.listForOwner(UUID.randomUUID()).shouldBeEmpty()
    }

    @Test
    fun `list for owner`() {
        repo.listForOwner(ownerId).shouldContainExactlyInAnyOrder(kratos, athena)
    }

    @Test
    fun `delete missing`() {
        repo -= UUID.randomUUID()
    }

    @Test
    fun `delete existing`() {
        repo -= kratos.id
        repo[kratos.id] shouldBe null
    }
}