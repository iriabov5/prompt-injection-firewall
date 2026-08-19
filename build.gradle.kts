import org.gradle.api.tasks.testing.AbstractTestTask
import com.google.devtools.ksp.gradle.KspExtension
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("org.jetbrains.kotlin.jvm") version "2.3.21"
    id("org.jetbrains.kotlin.plugin.allopen") version "2.3.21"
    id("com.google.devtools.ksp") version "2.3.7"
    id("io.micronaut.application") version "4.6.2"
    id("io.micronaut.aot") version "4.6.2"
    id("org.sonarqube") version "7.3.1.8318"
    jacoco
}

version = "0.1.0"
group = "com.ryabov.promptfirewall"

val kotlinVersion = providers.gradleProperty("kotlinVersion")

dependencies {
    ksp("io.micronaut:micronaut-http-validation")
    ksp("io.micronaut.data:micronaut-data-processor")
    ksp("io.micronaut.openapi:micronaut-openapi")
    ksp("io.micronaut.serde:micronaut-serde-processor")
    ksp("io.micronaut.validation:micronaut-validation-processor")

    implementation("io.micrometer:context-propagation")
    implementation("io.micronaut.data:micronaut-data-jdbc")
    implementation("io.micronaut.flyway:micronaut-flyway")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-management")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.micrometer:micronaut-micrometer-core")
    implementation("io.micronaut.reactor:micronaut-reactor")
    implementation("io.micronaut.reactor:micronaut-reactor-http-client")
    implementation("io.micronaut.security:micronaut-security")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.validation:micronaut-validation")
    implementation("io.swagger.core.v3:swagger-annotations")
    implementation("jakarta.validation:jakarta.validation-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion.get()}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion.get()}")

    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    runtimeOnly("org.flywaydb:flyway-database-postgresql")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.yaml:snakeyaml")

    testImplementation("io.mockk:mockk:1.14.6")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:jdbc")
    testImplementation("org.testcontainers:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

configure<KspExtension> {
    arg(
        "micronaut.openapi.views.spec",
        "mapping.path=swagger,swagger-ui.enabled=true,swagger-ui.theme=flattop"
    )
}

application {
    mainClass.set("com.ryabov.promptfirewall.ApplicationKt")
}

java {
    sourceCompatibility = JavaVersion.toVersion("21")
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

micronaut {
    version.set(providers.gradleProperty("micronautVersion"))
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.ryabov.promptfirewall.*")
    }
    aot {
        optimizeServiceLoading.set(false)
        convertYamlToJava.set(false)
        precomputeOperations.set(true)
        cacheEnvironment.set(true)
        optimizeClassLoading.set(true)
        deduceEnvironment.set(true)
        optimizeNetty.set(true)
        replaceLogbackXml.set(true)
    }
}

tasks.withType<AbstractTestTask>().configureEach {
    failOnNoDiscoveredTests = false
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "**/Application*",
                    "**/model/**",
                    "**/audit/PersistentAuditEvent*",
                    "**/ai/AiAnalysisResult*",
                    "**/ai/AiProperties*",
                    "**/ai/OpenAiChat*"
                )
            }
        })
    )
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)
    violationRules {
        rule {
            limit {
                minimum = "0.80".toBigDecimal()
            }
        }
    }
    classDirectories.setFrom(tasks.jacocoTestReport.get().classDirectories)
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

sonar {
    properties {
        property("sonar.projectKey", "prompt-injection-firewall")
        property("sonar.projectName", "Prompt Injection Firewall")
        property("sonar.host.url", "http://localhost:9000")
        property("sonar.sourceEncoding", "UTF-8")
        property("sonar.sources", "src/main/kotlin")
        property("sonar.tests", "src/test/kotlin")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            layout.buildDirectory.file("reports/jacoco/test/jacocoTestReport.xml").get().asFile.absolutePath
        )
    }
}

tasks.named("sonar") {
    dependsOn(tasks.test)
    dependsOn(tasks.jacocoTestReport)
}
