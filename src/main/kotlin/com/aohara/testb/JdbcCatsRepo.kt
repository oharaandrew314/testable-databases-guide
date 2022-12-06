package com.aohara.testb

import java.sql.ResultSet
import java.util.UUID
import javax.sql.DataSource

class JdbcCatsRepo(private val datasource: DataSource): CatsRepo {

    override fun plusAssign(cat: Cat) {
        datasource.connection.use { conn ->
            conn.prepareStatement("INSERT INTO cats (id, ownerId, name) VALUES (?, ?, ?)").use { stmt ->
                stmt.setString(1, cat.id.toString())
                stmt.setString(2, cat.ownerId.toString())
                stmt.setString(3, cat.name)

                stmt.executeUpdate()
            }
        }
    }

    override fun minusAssign(id: UUID) {
        datasource.connection.use { conn ->
            conn.prepareStatement("DELETE FROM cats WHERE id = ?").use { stmt ->
                stmt.setString(1, id.toString())

                stmt.executeUpdate()
            }
        }
    }

    override fun get(id: UUID): Cat? {
        datasource.connection.use { conn ->
            conn.prepareStatement("SELECT * FROM cats WHERE id = ?").use { stmt ->
                stmt.setString(1, id.toString())

                stmt.executeQuery().use { row ->
                    return row
                        .takeIf { row.next() }
                        ?.toCat()
                }
            }
        }
    }

    override fun listForOwner(ownerId: UUID): List<Cat> {
        datasource.connection.use { conn ->
            conn.prepareStatement("SELECT * FROM cats where ownerId = ?").use { stmt ->
                stmt.setString(1, ownerId.toString())

                stmt.executeQuery().use { row ->
                    return sequence {
                        while(row.next()) {
                            yield(row.toCat())
                        }
                    }.toList()
                }
            }
        }
    }

    private fun ResultSet.toCat() = Cat(
        id = UUID.fromString(getString("id")),
        ownerId = UUID.fromString(getString("ownerId")),
        name = getString("name")
    )
}