package com.challenge.redis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    exclude = arrayOf(DataSourceAutoConfiguration::class),
    scanBasePackages = arrayOf("com.challenge.redis")
)

class RedisChallengeApplication

fun main(args: Array<String>) {
    runApplication<RedisChallengeApplication>(*args)
}
