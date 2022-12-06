package com.aohara.testdb

import com.aohara.testb.CatsTable
import com.aohara.testb.ExposedCatsRepo
import org.h2.jdbcx.JdbcDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import javax.sql.DataSource

private fun testDb(): Database {
    val dataSource: DataSource = JdbcDataSource().apply {
        setURL("jdbc:h2:mem:${UUID.randomUUID()};MODE=MySQL;DB_CLOSE_DELAY=-1")
        this.user = "sa"
        this.password = ""
    }
    val db = Database.connect(dataSource)
    transaction(db) {
        SchemaUtils.create(CatsTable)
    }
    return db
}

class ExposedCatsRepoTest: CatsRepoContract(ExposedCatsRepo(testDb()))