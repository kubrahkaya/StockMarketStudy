buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(ClassPaths.gradle)
        classpath(ClassPaths.kotlinGradlePlugin)
        classpath(ClassPaths.daggerHiltGradlePlugin)
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}