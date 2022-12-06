package com.aohara.testdb

import com.aohara.testb.DynamoCat
import com.aohara.testb.V2DynamoCatsRepo
import io.andrewohara.awsmock.dynamodb.MockDynamoDbV2
import io.andrewohara.dynamokt.DataClassTableSchema
import io.andrewohara.dynamokt.createTableWithIndices
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient

private fun testTable() = DynamoDbEnhancedClient.builder()
    .dynamoDbClient(MockDynamoDbV2())
    .build()
    .table("cats", DataClassTableSchema(DynamoCat::class))
    .also { it.createTableWithIndices() }

class V2DynamoCatsRepoTest: CatsRepoContract(V2DynamoCatsRepo(testTable()))