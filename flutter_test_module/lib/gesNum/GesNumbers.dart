import 'dart:ffi';
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
  int successNum = 0;
  List historyNum = [];
  List gesNumHistory = [];
  List gesResult = [];

  //定义一个controller
  TextEditingController _gesController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return SingleChildScrollView(
      child: Column(
        // mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Container(
            width: 300,
            padding: EdgeInsets.only(bottom: 30),
            child: TextField(
              controller: _gesController,
              autofocus: true,
              decoration: InputDecoration(
                hintText: "猜数字，不输入默认为1",
              ),
            ),
          ),

          Text("猜数成功率" + _getResRate()),
          Text("历史数据"),
          Container(
            height: 1,
            color: Colors.lightBlue,
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              Text("随机数"),
              Text("猜数"),
              Text("结果"),
            ],
          ),
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
          Text("随机数  ： " + num.toString(), style: TextStyle(color: Colors.red),),
          ElevatedButton(
            child: const Text("随机数字"),
            onPressed: () {
              setState(() {
                num = random.nextInt(49) + 1; //左闭右开，+1
                historyNum.add(num);
                var gesNumStr = _gesController.text;
                int gesNum;
                try {
                  gesNum = int.parse(gesNumStr);
                } catch (e) {
                  gesNum = 1;
                }
                gesNumHistory.add(gesNum);
                bool gesres = gesNum == num;
                gesResult.add(gesres);
                if (gesres) {
                  successNum++;
                }
              });
            },
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
    int len = historyNum.length;
    for (int index = len - 1; index > 0; index--) {
      list.add(
        Container(
          color: _getColor(index),
          child: Row(
            mainAxisAlignment: MainAxisAlignment.spaceAround,
            children: [
              Text(historyNum[index].toString()),
              Text(gesNumHistory[index].toString()),
              Text(gesResult[index].toString()),
            ],
          ),
        ),
      );
    }
    return list;
  }

  String _getResRate() {
    double rate = (successNum / gesResult.length);
    return rate.toStringAsFixed(3);
  }

  Color _getColor(int index) {
    if(gesResult[index]) {
      return Colors.red;
    } else {
      return Colors.transparent;
    }
  }
}
