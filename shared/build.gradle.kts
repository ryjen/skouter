import java.util.*

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("com.apollographql.apollo3")
    id("com.github.gmazzo.buildconfig").version("3.1.0")
    kotlin("plugin.serialization") version "1.8.20"
}

kotlin {
    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.ui)
                api(compose.foundation)
                api(compose.materialIconsExtended)
                api(compose.material3)

                api("io.insert-koin:koin-core:3.4.0")

                api("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.5")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")

                implementation("com.chrynan.navigation:navigation-compose:0.7.0")

                implementation("io.ktor:ktor-client-cio:1.0.0")

                implementation("io.github.jan-tennert.supabase:gotrue-kt:0.9.2-dev")
                implementation("io.github.jan-tennert.supabase:postgrest-kt:0.9.2-dev")
                implementation("io.github.jan-tennert.supabase:apollo-graphql:0.9.2-dev")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.appcompat:appcompat:1.6.1")
                implementation("androidx.core:core-ktx:1.10.0")
                api("io.insert-koin:koin-androidx-compose:3.4.3")
                api(compose.preview)
                implementation("org.jetbrains.kotlinx:kotlin-deeplearning-api:0.5.1")
            }
        }
        val androidUnitTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "com.micrantha.skouter"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
}

apollo {
    service("service") {
        packageName.set("com.micrantha.skouter")
    }
}

fun localProperties(): Properties {
    val properties = Properties()
    properties.load(project.rootProject.file("local.properties").reader())
    return properties
}

val properties = localProperties()
val apiKey = properties["apiKey"]
val apiDomain = properties["apiDomain"]

buildConfig {
    buildConfigField("String", "API_KEY", "\"${apiKey}\"")
    buildConfigField("String", "API_DOMAIN", "\"${apiDomain}\"")
}
