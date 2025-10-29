// Define os plugins a serem aplicados a este módulo (app)
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.compose")
}

import java.io.File
import java.io.FileInputStream
import java.util.Properties

// Bloco de configuração específico do Android
android {
    namespace = "com.example.musiletra"
    compileSdk = 34 // Certifique-se que o SDK 34 está instalado

    defaultConfig {
        applicationId = "com.example.musiletra"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    val secretsFile = File(project.rootDir, "secrets.properties")
    if (secretsFile.exists()) {
        val properties = Properties()
        properties.load(FileInputStream(secretsFile))
        android.defaultConfig.buildConfigField("String", "AUDD_API_KEY", "\"${properties.getProperty("AUDD_API_KEY")}\"")
    } else {
        android.defaultConfig.buildConfigField("String", "AUDD_API_KEY", "\"\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Desativa a minificação para debug mais fácil
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        // Defina a compatibilidade Java (1.8 é comum para Android)
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        // Define a versão da JVM alvo para o Kotlin
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    // Configurações do Compose - A versão da extensão do compilador é crucial
    composeOptions {
        // Lê a versão do compilador definida no libs.versions.toml
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        // Resolve conflitos de licença comuns em projetos Compose
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// Bloco onde as dependências são declaradas
dependencies {
    // AndroidX Core e Lifecycle
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2023.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    
    // Material Icons Extended
    implementation("androidx.compose.material:material-icons-extended")

    // Networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // Debug
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}