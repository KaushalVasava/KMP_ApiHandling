# KMP_ApiHanding
It is a simple app where Home page shows all photos from Unplace api and FullImage page shows particular image using MVVM architecture.

# Technologies used:
- Kotlin Mutli Platform to Develop Apps for different platform like Android, Ios, Desktop, Web and MacOs.
- Kotlin for coding.
- Jetpack compose for UI development.
- Ktor client for API calling.
- MVVM architecture for better code reusability and management.
- Moko-Viewmodel for Viewmodel support in KMP.
- Navigation using PreCompose library.

# Project Setup
- Goto Android Studio
- Add Kotlin Multi Mobile for multi device support and Compose Multiplatform IDE support for development of Desktop app using Compose.
- Create New Project of Kotlin Multi-Platform App.
- Add below code in your `gradle.properties` file

  ```
  kotlin.mpp.enableCInteropCommonization=true
  kotlin.mpp.androidSourceSetLayoutVersion=2
    
  #Compose
  org.jetbrains.compose.experimental.uikit.enabled=true
    
  #Android
  android.useAndroidX=true
  android.nonTransitiveRClass=true
  android.compileSdk=33
  android.targetSdk=33
  android.minSdk=24
    
  #Versions
  kotlin.version=1.8.22
  agp.version=8.1.0
  compose.version=1.4.3
  ```
- Add this code in your `settings.gradle.kts` file
  ```
  pluginManagement {
     repository {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev") 
     }
  
     plugins {
        val kotlinVersion = extra["kotlin.version"] as String
        val agpVersion = extra["agp.version"] as String
        val composeVersion = extra["compose.version"] as String

        kotlin("jvm").version(kotlinVersion)
        kotlin("multiplatform").version(kotlinVersion)
        kotlin("android").version(kotlinVersion)

        id("com.android.application").version(agpVersion)
        id("com.android.library").version(agpVersion)
        id("org.jetbrains.compose").version(composeVersion)
    }
  }
  dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
  }
- Add plugins on your shared block `build.gradle.kts` file
  ```
    plugins {
      ...
      id("org.jetbrains.compose")
      kotlin("plugin.serialization") version "1.8.10"
   }

- Add these dependencies inside commonMain dependencies block
  ```
   implementation(compose.runtime)
   implementation(compose.foundation)
   implementation(compose.material)
   @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
   implementation(compose.components.resources)
   implementation("media.kamel:kamel-image:0.6.0")
   implementation("io.ktor:ktor-client-core:2.3.2")
   implementation("io.ktor:ktor-client-content-negotiation:2.3.2")
   implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.2")
   implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
   api("dev.icerock.moko:mvvm-core:0.16.1") // only ViewModel, EventsDispatcher, Dispatchers.UI
   api("dev.icerock.moko:mvvm-compose:0.16.1") // api mvvm-core, getViewModel for Compose Multiplatfrom
   // pre-compose for navigation
   api(compose.foundation)
   api(compose.animation)
   api("moe.tlaster:precompose:1.4.3")
- Create these blocks below commonMain block
  ```
  val androidMain by getting {
      dependencies {
           api("androidx.activity:activity-compose:1.7.2")
           api("androidx.appcompat:appcompat:1.6.1")
           api("androidx.core:core-ktx:1.10.1")
           implementation("io.ktor:ktor-client-android:2.3.2")
      }
  }
  val iosMain by getting {
      dependencies {
           implementation("io.ktor:ktor-client-darwin:2.3.1")
      }
  }
  val desktopMain by getting {
      dependencies {
           implementation(compose.desktop.common)
           implementation("io.ktor:ktor-client-apache:2.3.2")
      }
  }
- If you have Java 17 then add this code on your android block
  ```
   compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
   }         

# Desktop support steps:
- Add this code in you `settings.gradle.kts`
  ```
  include(":androidApp")
  include(":shared") 
  include(":desktopApp")

- Add this block below commonMain
  ```
  val desktopMain by getting {
      dependencies {
           implementation(compose.desktop.common)
           implementation("io.ktor:ktor-client-apache:2.3.2")
      }
  }
- Add these for Desktop support in kotlin block, before sourceSets block of shared(`build.gradle.kts`)
  ```
  jvm("desktop") {
      compilations.all {
          kotlinOptions.jvmTarget = "17"
      }
  }
- Add a new desktop folder at the top level of the app
- Add a new file build.gradle.kts into the desktop folder, and fill it with this content:      
  ```
  import org.jetbrains.compose.compose
  import org.jetbrains.compose.desktop.application.dsl.TargetFormat

  plugins {
      kotlin("multiplatform")
      id("org.jetbrains.compose") version "1.2.2"
  }
  
  group = "com.domain.project"
  version = "1.0.0"
  
  kotlin {
      jvm {
          withJava()
          compilations.all {
              kotlinOptions.jvmTarget = "11"
          }
      }
      sourceSets {
          val jvmMain by getting {
              kotlin.srcDirs("src/jvmMain/kotlin")
              dependencies {
                  implementation(compose.desktop.currentOs)
                  api(compose.runtime)
                  api(compose.foundation)
                  api(compose.material)
                  api(compose.ui)
                  api(compose.materialIconsExtended)
  
                  implementation(project(":shared"))
              }
          }
      }
  }
  
  compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "MyProject"
            macOS {
                bundleID = "com.domain.project"
            }
        }
    }
  }
- Click on "sync now" (Or File > Sync project with gradle files)
   - After the sync, the desktop folder should now be recognized as a module, indicated by a little square at the bottom right of the folder icon
 
- In the desktop module, create the folder chain src/jvmMain/kotlin
- In the just created kotlin folder create the Kotlin file Main, and fill it as follows:
  ```
  import androidx.compose.foundation.layout.fillMaxSize
  import androidx.compose.material.Surface
  import androidx.compose.ui.Modifier
  import androidx.compose.ui.window.Window
  import androidx.compose.ui.window.application
  import androidx.compose.ui.window.rememberWindowState
  import androidx.compose.material.*
  
  fun main() {
      application {
          val windowState = rememberWindowState()
  
          Window(
              onCloseRequest = ::exitApplication,
              state = windowState,
              title = "My Project"
          ) {
              Surface(modifier = Modifier.fillMaxSize()) {
                  Text(text = "Welcome to my Project")
              }
          }
      }
  }

- Add a run configuration of type Gradle with the following settings:

   Name: "desktopApp"
  
   Run: "run"
  
   Gradle Project: "project:desktop"
-  Create the file shared/src/desktopMain/kotiln/com.domain.project/Platform.kt, and fill it with this:
   ```
   package com.domain.project

   class DesktopPlatform : Platform {
       override val name: String = "Desktop"
   }
    
   actual fun getPlatform(): Platform = DesktopPlatform()
- You should now be able to run the desktop app

# Some IMP docs
- KOtlin Multi platform

  https://kotlinlang.org/docs/multiplatform.html

- Jetpack compose for UI development
 
  https://developer.android.com/jetpack/compose?gclid=EAIaIQobChMIxsLS5tT6gAMVedlMAh3sjgGgEAAYASAAEgKU-fD_BwE&gclsrc=aw.ds  
- PreCompose Navigation setup:

  https://github.com/Tlaster/PreCompose/blob/master/docs/setup.md

- Kamel Image loading

  https://github.com/Kamel-Media/Kamel

- Ktor client for API

  https://ktor.io/docs/routing-in-ktor.html

- Moko ViewModel

  https://github.com/icerockdev/moko-mvvm

# Screen Recording and Screenshots
![image](https://github.com/KaushalVasava/KMP_ApiHanding/assets/49050597/06ea0eed-f315-4408-ba98-4adcf403af6a)

![Screenshot 2023-08-26 205423](https://github.com/KaushalVasava/KMP_ApiHanding/assets/49050597/0101edc7-b32b-40ef-8998-78f188a70a38)


https://github.com/KaushalVasava/KMP_ApiHanding/assets/49050597/0a2a4eae-b988-43d9-bc23-7e937d5e675a


# Thank you
Follow me on LinkedIn, GitHub, Twitter, Instagram, Medium and add this repo to your favorite and share with your friends and colleague.
Happy coding 😊
Author: Kaushal Vasava






