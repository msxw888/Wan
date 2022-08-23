

import 'dart:ffi';

import 'package:flutter/foundation.dart';

class DeBugLog {

  static log(Object object) {
    if(kDebugMode) {
      print(object);
    }
  }
}