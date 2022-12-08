package com.aohara.testdb

import com.mysql.cj.jdbc.MysqlDataSource
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

object CatsTable: UUIDTable(name = "cats") {
    val ownerId = uuid("ownerId")
    val name = text("name")
}

class ExposedCatsRepo(private val database: Database): CatsRepo {
    override fun plusAssign(cat: Cat) {
        transaction(database) {
            CatsTable.insert {
                it[id] = cat.id
                it[ownerId] = cat.ownerId
                it[name] = cat.name
            }
        }
    }

    override fun minusAssign(id: UUID) {
        transaction(database) {
            CatsTable.deleteWhere { CatsTable.id eq id }
        }
    }

    override fun get(id: UUID) = transaction(database) {
        CatsTable
            .select { CatsTable.id eq id }
            .map { it.toCat() }
            .firstOrNull()
    }

    override fun listForOwner(ownerId: UUID) = transaction(database) {
        CatsTable
            .select { CatsTable.ownerId eq ownerId }
            .map { it.toCat() }
    }

    private fun ResultRow.toCat() = Cat(
        id = this[CatsTable.id].value,
        ownerId = this[CatsTable.ownerId],
        name = this[CatsTable.name]
    )
}

fun main() {
    val dataSource = MysqlDataSource().apply {
        databaseName = System.getenv("DB_NAME")
        serverName = System.getenv("DB_HOST")
        user = System.getenv("DB_USER")
        password = System.getenv("DB_PASS")
    }

    val database = Database.connect(dataSource)
    val repository = ExposedCatsRepo(database)
    // do stuff
}