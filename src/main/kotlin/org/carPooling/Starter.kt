package org.carPooling

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Starter

fun main(args: Array<String>) {
    runApplication<Starter>(*args)
}