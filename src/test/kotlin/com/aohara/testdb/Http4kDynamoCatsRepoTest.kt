package com.aohara.testdb

import com.aohara.testb.Cat
import com.aohara.testb.Http4kDynamoCatsRepo
import org.http4k.connect.amazon.dynamodb.FakeDynamoDb
import org.http4k.connect.amazon.dynamodb.mapper.tableMapper
import org.http4k.connect.amazon.dynamodb.model.TableName
import java.util.UUID

private fun testTable(tableName: TableName = TableName.of("cats")) = FakeDynamoDb()
    .client()
    .tableMapper<Cat, UUID, Unit>(tableName, Http4kDynamoCatsRepo.Schema.primaryIndex)
    .also { it.createTable(Http4kDynamoCatsRepo.Schema.ownerIndex) }

class Http4kDynamoCatsRepoTest: CatsRepoContract(Http4kDynamoCatsRepo(testTable()))