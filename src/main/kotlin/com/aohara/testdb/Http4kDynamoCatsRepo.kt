package com.aohara.testdb

import org.http4k.connect.amazon.dynamodb.DynamoDb
import org.http4k.connect.amazon.dynamodb.Http
import org.http4k.connect.amazon.dynamodb.mapper.DynamoDbTableMapper
import org.http4k.connect.amazon.dynamodb.mapper.DynamoDbTableMapperSchema
import org.http4k.connect.amazon.dynamodb.mapper.tableMapper
import org.http4k.connect.amazon.dynamodb.model.Attribute
import org.http4k.connect.amazon.dynamodb.model.IndexName
import org.http4k.connect.amazon.dynamodb.model.TableName
import java.util.UUID

class Http4kDynamoCatsRepo(private val table: DynamoDbTableMapper<Cat, UUID, Unit>): CatsRepo {

    object Schema {
        private val idAttr = Attribute.uuid().required("id")
        private val ownerIdAttr = Attribute.uuid().required("ownerId")
        private val ownerIndexName = IndexName.of("owners")

        val primaryIndex = DynamoDbTableMapperSchema.Primary<UUID, Unit>(idAttr, null)
        val ownerIndex = DynamoDbTableMapperSchema.GlobalSecondary(ownerIndexName, ownerIdAttr, idAttr)
    }

    private val ownerIndex = table.index(Schema.ownerIndex)

    override fun get(id: UUID) = table[id]
    override fun listForOwner(ownerId: UUID) = ownerIndex.query(ownerId).toList()
    override fun plusAssign(cat: Cat) = table.plusAssign(cat)
    override fun minusAssign(id: UUID) = table.delete(id)
}

fun main() {
    val tableName = TableName.of(System.getenv("TABLE"))

    val repository = DynamoDb.Http()
        .tableMapper<Cat, UUID, Unit>(tableName, Http4kDynamoCatsRepo.Schema.primaryIndex)
        .let { Http4kDynamoCatsRepo(it) }

    // do stuff
}