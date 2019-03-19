# jlocation

不使用 google 服务，不用注册第三方 key，可以直接获取设备 GPS 信息的 flutter 插件。暂时只支持 Android,后续考虑支持 iOS。

Android 使用，需要做以下修改
1. 在 gradle.properties 文件中增加
<pre>
android.enableJetifier=true
android.useAndroidX=true</pre>
2. 在 app/build.gradle 文件中增加
<pre>
dependencies {
    ... ...
    implementation 'com.android.support:support-v4:28.0.0'
    ... ...
}
</pre>
3. 在 AndroidManifest.xml 中增加
<pre>
&lt;uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" /&gt;
</pre>
4. 由于发布到 pub.dartlng.org 失败，所以引用操作是 在 pubspec.yaml 文件的 dependences 中增加
<pre>
  jlocation:
    git: https://github.com/seerx/jlocation.git
 </pre>

## Getting Started
Example：
<pre>
    var loc = new Jlocation(); 
    await loc.onLocationChanged().listen((loc) {
      setState(() {
        if (!mounted) return;
        _platformVersion = "${loc.last}=${loc.longitude},${loc.latitude}";
        print(_platformVersion);
      });
    });
</pre>

This project is a starting point for a Flutter
[plug-in package](https://flutter.io/developing-packages/),
a specialized package that includes platform-specific implementation code for
Android.

For help getting started with Flutter, view our 
[online documentation](https://flutter.io/docs), which offers tutorials, 
samples, guidance on mobile development, and a full API reference.
