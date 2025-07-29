plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("io.github.bonigarcia:webdrivermanager:5.8.0")
    implementation("com.github.stephenc.monte:monte-screen-recorder:0.7.7.0")
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("com.googlecode.json-simple:json-simple:1.1.1")

    // Cucumber Dependencies
    implementation("io.cucumber:cucumber-java:7.15.0")
    implementation("io.cucumber:cucumber-testng:7.15.0")

    // Selenium Dependency
    implementation("org.seleniumhq.selenium:selenium-java:4.14.1")

    // Hamcrest Dependency
    testImplementation("org.hamcrest:hamcrest:2.2")

    // Allure Cucumber Dependency
    implementation("io.qameta.allure:allure-cucumber7-jvm:2.29.0")

    // Rest Assured for testing
    testImplementation("io.rest-assured:rest-assured:5.5.1")

    // Apache POI
    implementation("org.apache.poi:poi:5.2.3")
    implementation("org.apache.poi:poi-ooxml:5.2.3")

    // TestNG for testing
  //  implementation("org.testng:testng:6.8.3")

    implementation ("com.sun.mail:javax.mail:1.6.2")
    implementation ("javax.activation:activation:1.1.1")

}
configurations.all {
    resolutionStrategy {
        force ("org.testng:testng:6.8.3")  // Compatible with Java 8
    }
}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

tasks.named<Test>("test") {
    useTestNG()
}
