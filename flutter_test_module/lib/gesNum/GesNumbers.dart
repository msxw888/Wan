import 'dart:math';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class GesNumbersPage extends StatefulWidget {
  const GesNumbersPage({Key? key}) : super(key: key);

  @override
  State<GesNumbersPage> createState() => _GesNumbersPageState();
}

class _GesNumbersPageState extends State<GesNumbersPage> {
  Random random = Random();
  int num = 0;
  List historyNum = [];

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Column(
        // mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Text("历史数据"),
          Container(
            height: 300,
            child: ListView(
              children: _getList(),
            ),
          ),
          Container(
            height: 1,
            color: Colors.lightBlue,
          ),
          Text(num.toString()),
          ElevatedButton(
            child: const Text("随机数字"),
            onPressed: () {
              setState(() {
                num = random.nextInt(49) + 1; //左闭右开，+1
                historyNum.add(num);
              });
            },
          ),

          Container(
            width: 300,
            child: TextField(

              autofocus: true,
              decoration: InputDecoration(
                  hintText: "用户名或邮箱",
              ),
            ),
          ),
        ],
      ),
    );
  }

  List<Widget> _getList() {
    List<Widget> list = [];
    if (historyNum == null || list.isNotEmpty) {
      return list;
    }
    for (int value in historyNum.reversed) {
      list.add(ListTile(
        title: Text(value.toString()),
      ));
    }
    return list;
  }
}
