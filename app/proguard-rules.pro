## Add project specific ProGuard rules here.
## By default, the flags in this file are appended to flags specified
## in /usr/local/sdk/tools/proguard/proguard-android.txt
## You can edit the include path and order by changing the proguardFiles
## directive in build.gradle.
##
## For more details, see
##   http://developer.android.com/guide/developing/tools/proguard.html
#
## Add any project specific keep options here:
#
## If your project uses WebView with JS, uncomment the following
## and specify the fully qualified class name to the JavaScript interface
## class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
##->设置混淆的压缩比率 0 ~ 7
#-optimizationpasses 5
##-> Aa aA
#-dontusemixedcaseclassnames
##->如果应用程序引入的有jar包,并且想混淆jar包里面的class
##-dontskipnonpubliclibraryclasses
#-dontpreverify
##>混淆后生产映射文件 map 类名->转化后类名的映射
##-verbose                                        -
##->混淆采用的算法.
##-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
##->所有activity的子类不要去混淆
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
#-keep public class com.android.vending.licensing.ILicensingService
##-> 所有native的方法不能去混淆.
#-keepclasseswithmembernames class * {
#    native <methods>;
#}
##-->某些构造方法不能去混淆
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet);
#}
#
#-keepclasseswithmembers class * {
#    public <init>(android.content.Context, android.util.AttributeSet, int);
#}
#
#-keepclassmembers class * extends android.app.Activity {
#   public void *(android.view.View);
#}
##-> 枚举类不能去混淆
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
##-> aidl文件不能去混淆.
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
#
#-keepclassmembers class * {
#   public <init>(org.json.JSONObject);
#}
#
#-keep class android.support.v4.**{*;}
#-keepattributes *Annotation*
#
#-libraryjars libs/aaa.jar
#-dontwarn com.xx.yy.**
#-keep class com.xx.yy.** { *;}
#
## Gson混淆脚本
#-keep class com.google.gson.stream.** {*;}
#-keep class com.youyou.uuelectric.renter.Network.user.** {*;}
#
## butterknife混淆脚本
#-dontwarn butterknife.internal.**
#-keep class **$$ViewInjector { *; }
#-keepnames class * { @butterknife.InjectView *;}
#
## Glide图片库的混淆处理
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
#  **[] $VALUES;
#  public *;
#}
