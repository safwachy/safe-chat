package com.safwachy.safechat

import com.safwachy.safechat.exception.ControllerExceptionHandler
import com.safwachy.safechat.repository.table.Message
import com.safwachy.safechat.repository.table.Room
import com.safwachy.safechat.repository.table.User
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.util.logging.Logger

@SpringBootApplication
class SafeChatApplication

private val LOG: Logger = Logger.getLogger(SafeChatApplication::class.java.name)

fun main(args: Array<String>) {
	// Connect to database
	LOG.info("Connecting to PostgresSQL database...")
	val dbUrl = System.getenv("DB_URL")
	val dbUser = System.getenv("DB_USER")
	val dbPass = System.getenv("DB_PASS")
	Database.connect(dbUrl, driver = "org.postgresql.Driver", user = dbUser, password = dbPass)

	// Set up database
	LOG.info("Setting database tables...")
	transaction {
		SchemaUtils.drop(Message)
		SchemaUtils.drop(User)
		SchemaUtils.drop(Room)

		SchemaUtils.create(Room)
		SchemaUtils.create(User)
		SchemaUtils.create(Message)
	}
	LOG.info("Database initialized")

	runApplication<SafeChatApplication>(*args)
}
