package net.packlister.packlister

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PacklisterApplication

fun main(args: Array<String>) {
    @Suppress("SpreadOperator") // runs once
    runApplication<PacklisterApplication>(*args)
}