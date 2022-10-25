package net.packlister.packlister

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableJpaAuditing // entity timestamps, improve: put in separate configuration in dao module
@EnableScheduling
class PacklisterApplication

fun main(args: Array<String>) {
    @Suppress("SpreadOperator") // runs once, no impact
    runApplication<PacklisterApplication>(*args)
}
