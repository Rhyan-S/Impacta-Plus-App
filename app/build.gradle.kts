plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "impacta.plus.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "impacta.plus.app"
        minSdk = 23 // Alterado de 24 para 23 (padrão de segurança atual, mas 24 também funciona)
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // --- NOVO: Habilita o ViewBinding (Adeus findViewById!) ---
    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // Bibliotecas Padrão (Mantidas do seu Version Catalog)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Testes (Mantidos)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // --- LIMPEZA E ADIÇÕES ---

    // 1. RecyclerView: Mantido (Essencial para as listas)
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // 2. Fragments: Mantido (Útil para a navegação)
    implementation("androidx.fragment:fragment-ktx:1.8.0")

    // 3. SEGURANÇA (NOVO): Biblioteca oficial do Google para criptografia
    // Necessária para usar 'EncryptedSharedPreferences' e 'MasterKey' no SessionManager
    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // NOTA: Removi a dependência "androidx.cardview:cardview".
    // Motivo: A biblioteca "com.google.android.material" (que você já tem ali em cima)
    // já inclui o componente 'MaterialCardView', que é mais moderno e bonito que o CardView antigo.
}