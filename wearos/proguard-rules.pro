# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep Gson annotations
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class com.luwaiwong.nominal.wear.data.** { *; }

# Keep launch data models
-keep class com.luwaiwong.nominal.wear.data.Launch { *; }
-keep class com.luwaiwong.nominal.wear.data.LaunchProvider { *; }
-keep class com.luwaiwong.nominal.wear.data.Rocket { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
