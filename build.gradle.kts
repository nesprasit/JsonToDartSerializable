plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "1.9.0"
  id("org.jetbrains.intellij") version "1.15.0"
}

group = "com.nesprasit"
version = "1.0.1"

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
  pluginName.set("JsonToDartSerializable")
  version.set("AI-231.9392.1.2311.11255304")
  type.set("AI") // Target IDE Platform

  plugins.set(listOf("Dart:231.9411","android"))
}

tasks {

  // Set the JVM compatibility versions
  withType<JavaCompile> {
    sourceCompatibility = "17"
    targetCompatibility = "17"
  }

  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
  }

  patchPluginXml {
    version.set("1.0.1")
    sinceBuild.set("145.0")
  }

  signPlugin {
    certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
    privateKey.set(System.getenv("PRIVATE_KEY"))
    password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
  }

  publishPlugin {
    token.set(System.getenv("PUBLISH_TOKEN"))
  }

  test {
    useJUnitPlatform()
  }
}

tasks.register("buildRelease", type = Copy::class) {
  dependsOn(tasks["buildPlugin"])

  val dir = System.getProperty("user.dir")
  val from = file("$dir/build/libs/")
  val target = file("$dir/buildLib/libs/")

  from.listFiles()?.findLast { it.name.contains(Regex("^Json.*")) }?.let {
    from(it)
    into(target)
  }

  dependsOn(":buildLib:buildZip")
}
