package com.safwachy.safechat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SafeChatApplication

fun main(args: Array<String>) {
	runApplication<SafeChatApplication>(*args)
}
