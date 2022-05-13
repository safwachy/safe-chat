package com.safwachy.safechat.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

interface RootController {
    fun welcome() : ResponseEntity<Any?>
}

@RestController
class RootControllerImpl : RootController {
    @GetMapping("/")
    override fun welcome() : ResponseEntity<Any?> {
        return ResponseEntity("""
            <html>
                <head>
                    <title>SafeChat</title>
                </head>
                <body>
                    <h2>Welcome to SafeChat</h2>
                    <p>
                        For more information on the API, have a look at the source code <a href="https://github.com/safwachy/safe-chat">here</a>                 
                    </p>
                </body>
            </html>
        """.trimIndent(), HttpStatus.OK)
    }
}