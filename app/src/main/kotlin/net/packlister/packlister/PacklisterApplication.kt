package net.packlister.packlister

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing // entity timestamps, improve: put in separate configuration in dao module
class PacklisterApplication

fun main(args: Array<String>) {
    @Suppress("SpreadOperator") // runs once, no impact
    runApplication<PacklisterApplication>(*args)
}
