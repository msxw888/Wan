import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';
import 'package:webview_flutter/webview_flutter.dart';

class WebViewExample extends StatefulWidget {
  var url;

  var title;

  WebViewExample({this.url, this.title});

  @override
  WebViewExampleState createState() => WebViewExampleState(url, title);
}

class WebViewExampleState extends State<WebViewExample> {
  String url;
  String title;

  WebViewExampleState(this.url, this.title) {
    print("wjh url " + url);
  }

  @override
  void initState() {
    super.initState();
    // Enable virtual display.
    // print("wjh url " + url);
    if (Platform.isAndroid) WebView.platform = AndroidWebView();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text(title),
        ),
        body: WebView(
            // initialUrl: 'https://flutter.dev',
            initialUrl: url.toString(),
            javascriptMode: JavascriptMode.unrestricted,
            onProgress: (int progress) {
              print('WebView is loading (progress : $progress%)');
            }));
  }
}
