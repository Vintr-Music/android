plugins {
    `kotlin-dsl`
}

dependencies {
    // Expose version catalog accessors to precompiled script plugins.
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(libs.gradlePlugin.kotlin)
    implementation(libs.gradlePlugin.composeCompiler)
    implementation(libs.gradlePlugin.composeMultiplatform)
    implementation(libs.gradlePlugin.android)
}
