package com.aohara.testdb

import io.andrewohara.dynamokt.DataClassTableSchema
import io.andrewohara.dynamokt.DynamoKtPartitionKey
import io.andrewohara.dynamokt.DynamoKtSecondaryPartitionKey
import io.andrewohara.dynamokt.DynamoKtSecondarySortKey
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest
import java.util.UUID

class V2DynamoCatsRepo(private val mapper: DynamoDbTable<DynamoCat>): CatsRepo {

    override fun get(id: UUID) = mapper
        .getItem(id.toKey())
        ?.toCat()

    override fun plusAssign(cat: Cat) {
        mapper.putItem(cat.toDynamo())
    }

    override fun minusAssign(id: UUID) {
        mapper.deleteItem(id.toKey())
    }

    override fun listForOwner(ownerId: UUID): List<Cat> {
        val req = QueryEnhancedRequest.builder()
            .queryConditional(QueryConditional.keyEqualTo(ownerId.toKey()))
            .build()

        return mapper.index("owner")
            .query(req)
            .flatMap { it.items() }
            .map { it.toCat() }
    }
}

private fun UUID.toKey() = Key.builder().partitionValue(toString()).build()

data class DynamoCat(
    @DynamoKtPartitionKey
    @DynamoKtSecondarySortKey(["owner"])
    val id: UUID,

    @DynamoKtSecondaryPartitionKey(["owner"])
    val ownerId: UUID,

    val name: String
)

private fun DynamoCat.toCat() = Cat(id = id, ownerId = ownerId, name = name)
private fun Cat.toDynamo() = DynamoCat(id = id, ownerId = ownerId, name = name)

fun main() {
    val repository = DynamoDbEnhancedClient.create()
        .table(System.getenv("TABLE_NAME"), DataClassTableSchema(DynamoCat::class))
        .let { V2DynamoCatsRepo(it) }

    // do stuff
}