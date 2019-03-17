import 'dart:async';

import 'package:flutter/services.dart';

class LocationData {
  final bool last;
  final double latitude;
  final double longitude;
  final double accuracy;
  final double altitude;
  final double speed;
  final double speedAccuracy;

  LocationData._(
      this.last,
      this.latitude,
      this.longitude,
      this.accuracy,
      this.altitude,
      this.speed,
      this.speedAccuracy,
      );

  factory LocationData.fromMap(Map<String, double> dataMap) {
    bool last = dataMap["last"] == 1.0;
    return LocationData._(
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
  Stream<LocationData> _onLocationChanged;

  static const MethodChannel _channel = const MethodChannel('xval.cn/jlocation');
  static const EventChannel _stream = const EventChannel('xval.cn/jlocationstream');

  Future<bool> hasPermission() =>
      _channel.invokeMapMethod('hasPermission').then((result) => result ==1);

  /// Returns a stream of location information.
  Stream<LocationData> onLocationChanged() {
    if (_onLocationChanged == null) {
      _onLocationChanged = _stream
          .receiveBroadcastStream()
          .map<LocationData>(
              (element) => LocationData.fromMap(element.cast<String, double>())
          );
    }
    return _onLocationChanged;
  }

  Future<void> setProperty(int minTime, double minDistance) async {
    int res = await _channel.invokeMethod("setProperty", {
      "minTime": minTime, "minDistance": minDistance
    });

    print(res);
  }

//  static Future<String> get platformVersion async {
//    final String version = await _channel.invokeMethod('getPlatformVersion');
//    return version;
//  }
}