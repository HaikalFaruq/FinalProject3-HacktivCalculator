
@file:Suppress("DSL_SCOPE_VIOLATION")

buildscript {
    extra["agp_version"] = "8.1.0"
    extra["kotlin_version"] = "1.9.0"
    extra["ksp_version"] = "${extra["kotlin_version"]}-1.0.12"
    extra["compose_compiler_version"] = "1.5.1"
    extra["room_version"] = "2.5.2"
    extra["hilt_version"] = "2.47"
}

plugins {
    id("com.android.test") version "${project.extra["agp_version"]}" apply false
    id("com.android.application") version "${project.extra["agp_version"]}" apply false
    id("com.android.library") version "${project.extra["agp_version"]}" apply false
    kotlin("android") version "${project.extra["kotlin_version"]}" apply false
    id("com.google.devtools.ksp") version "${project.extra["ksp_version"]}" apply false
    id("com.google.dagger.hilt.android") version "${project.extra["hilt_version"]}" apply false
}
