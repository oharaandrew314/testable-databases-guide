plugins {
    kotlin("jvm") version "1.7.20"
}

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    implementation(platform("org.http4k:http4k-connect-bom:3.25.4.0"))

    implementation("org.jetbrains.exposed:exposed-jdbc:0.40.1")
    implementation("org.http4k:http4k-connect-amazon-dynamodb")
    implementation("com.github.oharaandrew314:dynamodb-kotlin-module:0.2.0")
    implementation("software.amazon.awssdk:dynamodb-enhanced:2.18.24")
    implementation("mysql:mysql-connector-java:8.0.30")

    testImplementation(kotlin("test"))
    testImplementation("com.h2database:h2:2.1.214")
    testImplementation("org.http4k:http4k-connect-amazon-dynamodb-fake")
    testImplementation("io.kotest:kotest-assertions-core:5.5.4")
    testImplementation("com.github.oharaandrew314:mock-aws-java-sdk:1.1.0")
}

tasks.test {
    useJUnitPlatform()
}