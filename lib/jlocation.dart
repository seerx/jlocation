import 'dart:async';

import 'package:flutter/services.dart';

class JLocData {
  final bool last;
  final double latitude;
  final double longitude;
  final double accuracy;
  final double altitude;
  final double speed;
  final double speedAccuracy;

  JLocData._(
      this.last,
      this.latitude,
      this.longitude,
      this.accuracy,
      this.altitude,
      this.speed,
      this.speedAccuracy,
      );

  factory JLocData.fromMap(Map<String, double> dataMap) {
    bool last = dataMap["last"] == 1.0;
    return JLocData._(
      last,
      dataMap['latitude'],
      dataMap['longitude'],
      dataMap['accuracy'],
      dataMap['altitude'],
      dataMap['speed'],
      dataMap['speed_accuracy'],
    );
  }
}

class Jlocation {
  Stream<JLocData> _onLocationChanged;

  static const MethodChannel _channel = const MethodChannel('xval.cn/jlocation');
  static const EventChannel _stream = const EventChannel('xval.cn/jlocationstream');

  Future<bool> hasPermission() =>
      _channel.invokeMapMethod('hasPermission').then((result) => result ==1);

  /// Returns a stream of location information.
  Stream<JLocData> onLocationChanged() {
    if (_onLocationChanged == null) {
      _onLocationChanged = _stream
          .receiveBroadcastStream()
          .map<JLocData>(
              (element) => JLocData.fromMap(element.cast<String, double>())
          );
    }
    return _onLocationChanged;
  }

  Future<bool> stopListen() =>
    _channel.invokeMethod('stopListen').then((result) => result == 1);

  Future<JLocData> getLocation() => _channel
      .invokeMethod('getLocation')
      .then((result) => JLocData.fromMap(result.cast<String, double>()));

//  Future<JLocData> getLocation() {
//    _channel.invokeMethod("getLocation")
//      .then((result) {
//        return JLocData.fromMap(result.cast<String, double>());
//    });
//  }

//  static Future<String> get platformVersion async {
//    final String version = await _channel.invokeMethod('getPlatformVersion');
//    return version;
//  }
}