import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test_module/bean/artical_entity.dart';
import 'package:flutter_test_module/generated/json/base/json_convert_content.dart';
import 'package:flutter_test_module/ui/ItemView.dart';

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
  List<Widget> _getList() {
    List<Widget> list = [];
    if (netData == null || list.isNotEmpty) {
      return list;
    }
    ArticalData data = netData!.data;
    List<ArticalDataDatas> dataList = data.datas;
    for (ArticalDataDatas value in dataList) {
      list.add(ListTile(
        title: Text(value.title),
      ));
    }
    return list;
  }

  @override
  Widget build(BuildContext context) {
    // This method is rerun every time setState is called, for instance as done
    // by the _incrementCounter method above.
    //
    // The Flutter framework has been optimized to make rerunning build methods
    // fast, so that you can just rebuild anything that needs updating rather
    // than having to individually change instances of widgets.
    return Scaffold(
        appBar: AppBar(
          leading: IconButton(
            icon: const Icon(
              Icons.menu,
              semanticLabel: "mennu",
            ),
            onPressed: () {
              print('Menu button');
            },
          ),
          actions: <Widget>[
            IconButton(
              icon: const Icon(
                Icons.search,
                semanticLabel: 'search',
              ),
              onPressed: () {
                print('Search button');
              },
            ),
            IconButton(
              icon: const Icon(
                Icons.tune,
                semanticLabel: 'filter',
              ),
              onPressed: () {
                print('Filter button');
              },
            ),
          ],
          // Here we take the value from the MyHomePage object that was created by
          // the App.build method, and use it to set our appbar title.
          title: Text(widget.title),
        ),
        body: Center(
          // Center is a layout widget. It takes a single child and positions it
          // in the middle of the parent.
          child: ListView(children: _getList()),
        )
        // floatingActionButton: FloatingActionButton(
        //   onPressed: _incrementCounter,
        //   tooltip: 'Increment',
        //   child: const Icon(Icons.add),
        // ), // This trailing comma makes auto-formatting nicer for build methods.
        );
  }

  @override
  void initState() {
    print("wjh");
    getHttp();
  }

  late ArticalEntity? netData = null;

  void getHttp() async {
    try {
      var response =
          await Dio().get('https://www.wanandroid.com/article/list/0/json');
      print("wjh response");
      setState(() {
        // netData = json.decode(response.data.toString());
        netData = JsonConvert.fromJsonAsT<ArticalEntity>(response.data);
        print("wjh setState");
        print(response);
      });
      // print(response);
    } catch (e) {
      print("wjh error");
      print(e);
    }
  }
}
