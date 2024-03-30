This is a Kotlin Multiplatform project targeting Android, iOS.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

  
<img width="1440" alt="Screenshot 2024-03-30 at 22 22 55" src="https://github.com/ime01/KMPTodoApp/assets/44091450/43c1e99a-6a42-4fe8-979b-33cc35ac2513">
<img width="1440" alt="Screenshot 2024-03-30 at 22 23 16" src="https://github.com/ime01/KMPTodoApp/assets/44091450/4e25bd6a-3a2a-44bf-a52b-c6f53508b8c5">


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
