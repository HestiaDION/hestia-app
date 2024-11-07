plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.hestia_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.hestia_app"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures{
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    viewBinding{
        enable=true
    }
}

dependencies {

    // Firebase BoM (Bill of Materials) para gerenciar versões de dependências do Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))

    // Glide para carregamento e manipulação eficiente de imagens
    implementation("com.github.bumptech.glide:glide:4.14.2")

    // AppCompat para compatibilidade com versões antigas do Android
    implementation("androidx.appcompat:appcompat:1.7.0")

    // Material Components para utilizar os componentes de design do Google Material
    implementation("com.google.android.material:material:1.12.0")

    // ConstraintLayout para criar layouts com restrições flexíveis e dinâmicas
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Lottie para renderizar animações no formato JSON do Adobe After Effects
    implementation("com.airbnb.android:lottie:6.3.0")

    // ViewPager2 para criar navegação swipeable entre fragmentos
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-storage:21.0.0")
    implementation("com.google.firebase:firebase-firestore:25.1.0")

    // JUnit para testes unitários
    testImplementation("junit:junit:4.13.2")

    // Extensão do JUnit para testes de UI no Android
    androidTestImplementation("androidx.test.ext:junit:1.2.1")

    // Espresso para automação de testes de UI no Android
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // CircleImageView para criar imagens de perfil circulares
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    // Splash Screen API para a tela de splash nas versões recentes do Android
    implementation("androidx.core:core-splashscreen:1.0.0")

    // Retrofit e Gson para fazer chamadas HTTP
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // câmera
    implementation("androidx.camera:camera-core:1.2.2")
    implementation("androidx.camera:camera-camera2:1.2.2")
    implementation("androidx.camera:camera-lifecycle:1.2.2")
    implementation("androidx.camera:camera-video:1.2.2")
    implementation("androidx.camera:camera-view:1.2.2")
    implementation("androidx.camera:camera-extensions:1.2.2")
    implementation ("com.google.guava:guava:31.1-android")

    // Material 3
    implementation("com.google.android.material:material:1.8.0")


    // debug
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    // viewModel e liveData
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.5.1")
    implementation("androidx.lifecycle:lifecycle-livedata:2.5.1")

    // debug
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    // worker
    implementation("androidx.work:work-runtime:2.7.0")



}