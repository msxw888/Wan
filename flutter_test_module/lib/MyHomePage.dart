import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test_module/bean/artical_entity.dart';
import 'package:flutter_test_module/generated/json/base/json_convert_content.dart';
import 'package:flutter_test_module/page/pages.dart';
import 'package:flutter_test_module/title/TitleBar.dart';
import 'package:flutter_test_module/ui/ItemView.dart';
import 'package:flutter_test_module/webview/WebViewExample.dart';
import 'package:fluttertoast/fluttertoast.dart';

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int currentIndex = 0;

  final pages = [
    const HomePage(),
    const MsgPage(),
    const CartPage(),
    const PersonPage()
  ];

  @override
  void initState() {
    print("wjh");
  }

  final List<BottomNavigationBarItem> bottomNavItems = [
    const BottomNavigationBarItem(
      backgroundColor: Colors.blue,
      icon: Icon(Icons.home),
      label: "首页",
    ),
    const BottomNavigationBarItem(
      backgroundColor: Colors.green,
      icon: Icon(Icons.message),
      label: "消息",
    ),
    const BottomNavigationBarItem(
      backgroundColor: Colors.amber,
      icon: Icon(Icons.shopping_cart),
      label: "购物车",
    ),
    const BottomNavigationBarItem(
      backgroundColor: Colors.red,
      icon: Icon(Icons.person),
      label: "个人中心",
    ),
  ];

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
      appBar: TitleBar(widget: widget),
      body: GestureDetector(
        behavior: HitTestBehavior.translucent,
        child: Container(
            child: pages[currentIndex],
            width: double.infinity,
            height: double.infinity),
        onHorizontalDragUpdate: (DragUpdateDetails details) {
          print(details);
        },
        onHorizontalDragEnd: (DragEndDetails details) {
          print(details.primaryVelocity);
          int index;
          if (details.primaryVelocity! < 0) {
            index = currentIndex + 1;
          } else {
            index = currentIndex - 1;
          }
          if (index < 0 || index > pages.length) {
            return;
          }
          _changePage(index);
          print(currentIndex);
        },
      ),
      // floatingActionButton: FloatingActionButton(
      // onPressed: _incrementCounter,
      // tooltip: 'Increment',
      // child: const Icon(Icons.add),
      // ), // This trailing comma makes auto-formatting nicer for build methods.
      bottomNavigationBar: BottomNavigationBar(
        items: bottomNavItems,
        currentIndex: currentIndex,
        type: BottomNavigationBarType.shifting,
        onTap: (index) {
          _changePage(index);
        },
      ),
    );
  }

  /*切换页面*/
  void _changePage(int index) {
    /*如果点击的导航项不是当前项  切换 */
    if (index != currentIndex) {
      setState(() {
        currentIndex = index;
      });
    }
  }
}
