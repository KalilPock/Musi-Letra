// Define os plugins a serem aplicados a este módulo (app)
// Usando id() para garantir a aplicação correta dos plugins essenciais
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Aplica o plugin do Compilador Compose pelo ID, a versão virá das configurações gerais
    id("org.jetbrains.kotlin.plugin.compose")
}

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
        // Habilita o Jetpack Compose
        compose = true
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

    // Core & Lifecycle
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose) // ViewModel Compose

    // Compose BOM e dependências Compose UI (versões geridas pelo BOM)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended) // Ícones extendidos

    // Networking: Retrofit, Gson, OkHttp (Verifique se os aliases estão no .toml)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp.logging.interceptor)

    // Testes Unitários
    testImplementation(libs.junit)

    // Testes de Instrumentação (UI)
    androidTestImplementation(libs.androidx.test.ext.junit) // Verifique o alias no .toml
    androidTestImplementation(libs.androidx.test.espresso.core) // Verifique o alias no .toml
    androidTestImplementation(platform(libs.androidx.compose.bom)) // BOM para testes
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Ferramentas de Debug (Compose)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}