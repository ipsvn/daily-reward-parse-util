plugins {
    java
    `maven-publish`
}

project.group   = "lol.svn"
project.version = "1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

repositories {
    mavenCentral()
}

dependencies {

    compileOnly("org.jetbrains:annotations:23.0.0")

    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("org.json:json:20220924")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")

}

tasks {
    test {
        useJUnitPlatform()
    }
}

publishing {
    publications {
        create<MavenPublication>("library") {
            from(components.getByName("java"))
            pom.name.set("daily-reward-parse-util")
            pom.description.set("Parses the Hypixel daily reward website into a basic Java API")
            pom.url.set("https://github.com/ipsvn/daily-reward-parse-util")
        }
    }
    repositories {
        maven {
            url = uri("${buildDir}/publishing-repository")
        }
        maven {
            name = "ipsvn"
            url = uri("https://m2.svn.lol/releases")
            credentials {
                username = property("m2-svn-username").toString()
                password = property("m2-svn-password").toString()
            }
        }
    }
}