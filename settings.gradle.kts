rootProject.name = "car-pooling"

pluginManagement {
    val kotlinVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val springFrameworkVersion: String by settings

    plugins {
        id("org.springframework.boot") version springFrameworkVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.spring") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
    }
}