fun properties(key: String) = project.findProperty(key).toString()
plugins {
    // Java support
    id("java")
    // Gradle IntelliJ Plugin
    id("org.jetbrains.intellij") version "1.3.0"
}
group = properties("pluginGroup")
version = properties("pluginVersion")
// Configure project's dependencies
repositories {
    mavenCentral()
}
// Configure Gradle IntelliJ Plugin - read more: https://github.com/JetBrains/gradle-intellij-plugin
intellij {
    pluginName.set(properties("pluginName"))
    version.set(properties("platformVersion"))
    type.set(properties("platformType"))
    // Plugin Dependencies. Uses `platformPlugins` property from the gradle.properties file.
    plugins.set(properties("platformPlugins").split(',').map(String::trim).filter(String::isNotEmpty))
}
tasks {
    // Set the JVM compatibility versions
    properties("javaVersion").let {
        withType<JavaCompile> {
            sourceCompatibility = it
            targetCompatibility = it
        }
    }
    wrapper {
        gradleVersion = properties("gradleVersion")
    }
    patchPluginXml {
        version.set(properties("pluginVersion"))
        sinceBuild.set(properties("pluginSinceBuild"))
        untilBuild.set(properties("pluginUntilBuild"))
        changeNotes.set("""
            <ul>
              <li>v1.1.0 Bugfix: Updated to run on MacOS Catalina and later</li>
              <li>v1.0.3 Bugfix: Updated deprecated Class</li>
              <li>v1.0.2 Bugfix: Evocation via keyboard shortcut w/o any file selected caused null pointer exception</li>
              <li>v1.0.1 Added: Keyboard shortcut - [CMD] + [I]</li>
              <li>v1.0.0 Initial Release</li>
            </ul>
            """.trimIndent())
    }
}
dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
tasks.getByName<Test>("test") {
    useJUnitPlatform()
}