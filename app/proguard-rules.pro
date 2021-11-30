# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# add 08-02-2021 (retrofit 2 not work release version, cutting GET-parameters)
-dontnote okhttp3.**, okio.**, retrofit2.**
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

# add 14-04-21 for supress R8 messages - Missing class... (after up retrofit from 2.4.0 to 2.6.4)
-dontwarn kotlin.coroutines.jvm.internal.ContinuationImpl
-dontwarn kotlin.jvm.internal.Lambda
-dontwarn kotlin.jvm.functions.Function1
# add 14-04-21 for supress R8 messages - Missing class... (after up okhttp3:logging-interceptor:3.12.8 from 3.8.0)
-dontwarn com.squareup.okhttp.Interceptor



# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
#-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
#-dontwarn retrofit2.KotlinExtensions
#-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>



# my edit 08-02-2021 11-30
-dontwarn androidx.annotation.**

-keepclassmembers, allowobfuscation class * {
  @ com.google.gson.annotations.SerializedName <fields>;
}



# my edit 08-02-2021 13-15

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-keepnames class com.squareup.okhttp.Interceptor

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform
-dontwarn org.conscrypt.ConscryptHostnameVerifier


# my edit 08-02-2021 13-15

#-addconfigurationdebugging
#-dontobfuscate

-keep class com.google.gson.stream.** { *; }

# Start crashlytics settings
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# add 08-03-2021 (problem with FirebaseCrashlytics)
-keep class com.firebase.** { *; }
-keep class com.google.firebase.** { *; }
-keepclassmembers enum com.google.android.datatransport.Priority {
    public static **[] values();
}
-dontwarn com.google.auto.value.AutoValue
-dontwarn com.google.auto.value.AutoValue$Builder
-keepclassmembers enum com.google.android.datatransport.Priority {
     *;
}
#-keepnames class com.astudio.progressmonitor.beans.** { *; }
#-keepattributes Signature
#-keepclassmembers class com.astudio.progressmonitor.** {
#    *;
#}

# End crashlytics settings

# my edit 08-02-2021 15-20
#-keep class com.test.apppackagename.retrofit.dto** {
#    *;
#}

# save config-info
#-printconfiguration ~/tmp/full-r8-config.txt




# add 09-02-2021

# Keep setters in Views so that animations can still work.

#-keepclassmembers public class * extends android.view.View {
#    void set*(***);
#    *** get*();
#}



# We want to keep methods in Activity that could be used in the XML attribute onClick.

#-keepclassmembers class * extends android.app.Activity {
#    public void *(android.view.View);
#}

#-keepclassmembers class * implements android.os.Parcelable {
#    public static final ** CREATOR;
#}

# add 06-03-2021 (при ребилде - R8 : warning : Missing class: android.arch.paging.PositionalDataSource)
-keep public class androidx.work.impl.** { *; }
-keep public class androidx.paging.PositionalDataSource { *; }
-dontwarn androidx.paging.PositionalDataSource


# add 07-03-2021 (UnsatisfiedLinkError: No implementation found then minifyEnabled true)
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}

-keep public class com.android.tools.profiler.agent.okhttp.** { *; }

#-keep class android.animation.ObjectAnimator { *; } # Add 17-03-2021 - for MPAndriodChart.animation

-keep class com.github.mikephil.charting** { *; } # Add 17-03-2021 - for MPAndriodChart.animation

# add 11-10-2021 for leak canary
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

# https://www.guardsquare.com/en/products/proguard/manual/examples#enumerations
-keep public enum leakcanary.AndroidLeakFixes {
  public static **[] values();
  public static ** valueOf(java.lang.String);
}

-keep class com.android.vending.billing.**
