package com.aohara.testdb

import com.aohara.testb.Cat
import com.aohara.testb.CatsRepo
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.util.UUID

private val owner1 = UUID.randomUUID()
private val owner2 = UUID.randomUUID()

private val kratos = Cat(
    id = UUID.randomUUID(),
    ownerId = owner1,
    name = "Kratos"
)

private val athena = Cat(
    id = UUID.randomUUID(),
    ownerId = owner1,
    name = "Athena"
)

private val smokie = Cat(
    id = UUID.randomUUID(),
    ownerId = owner2,
    name = "Smokie"
)

private val bandit = Cat(
    id = UUID.randomUUID(),
    ownerId = owner2,
    name = "Bandit"
)

abstract class CatsRepoContract(private val repo: CatsRepo) {

    init {
        repo += kratos
        repo += athena
        repo += smokie
        repo += bandit
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
        repo.listForOwner(owner1).shouldContainExactlyInAnyOrder(kratos, athena)
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