package com.aohara.testdb

import org.h2.jdbcx.JdbcDataSource
import java.util.UUID
import javax.sql.DataSource

private fun testDb(): DataSource {
    val dataSource = JdbcDataSource().apply {
        setURL("jdbc:h2:mem:${UUID.randomUUID()};MODE=MySQL;DB_CLOSE_DELAY=-1")
        this.user = "sa"
        this.password = ""
    }
    dataSource.connection.use { conn ->
        conn.prepareStatement(
            "CREATE TABLE cats (id CHAR(36) PRIMARY KEY NOT NULL, ownerId CHAR(36) NOT NULL, name TEXT NOT NULL)"
        ).executeUpdate()
    }
    return dataSource
}

class JdbcCatsRepoTest: CatsRepoContract(JdbcCatsRepo(testDb()))