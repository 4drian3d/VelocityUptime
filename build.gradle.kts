plugins {
    java
    alias(libs.plugins.runvelocity)
    alias(libs.plugins.idea.ext)
    alias(libs.plugins.blossom)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    clean {
        // Deletes the directory that is generated by the runVelocity plugin
        delete("run")
    }
    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }
}

sourceSets {
    main {
        blossom {
            javaSources {
                property("version", project.version.toString())
            }
        }
    }
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))
