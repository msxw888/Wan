import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_test_module/util/DeBugLog.dart';

import '../bean/artical_entity.dart';
import '../generated/json/base/json_convert_content.dart';
import '../webview/WebViewExample.dart';

class HomePage extends StatefulWidget {
  const HomePage({Key? key}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  // @override
  // Widget build(BuildContext context) {
  //
  //
  //   return Text("HomePage");
  //
  // }

  @override
  State<StatefulWidget> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  late ArticalEntity? netData = null;

  @override
  void initState() {
    getHttp();
  }

  @override
  Widget build(BuildContext context) {
    return Center(
      // Center is a layout widget. It takes a single child and positions it
      // in the middle of the parent.
      child: ListView(children: _getList()),
    );
  }

  void getHttp() async {
    try {
      var response =
          await Dio().get('https://www.wanandroid.com/article/list/0/json');
      if (kDebugMode) {
        DeBugLog.log("wjh response");
      }
      setState(() {
        // netData = json.decode(response.data.toString());
        netData = JsonConvert.fromJsonAsT<ArticalEntity>(response.data);
        DeBugLog.log("wjh setState");
        DeBugLog.log(response);
      });
      // DeBugLog.log(response);
    } catch (e) {
      DeBugLog.log("wjh error");
      DeBugLog.log(e);
    }
  }

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
        onTap: () {
          // Fluttertoast.showToast(
          //     msg: value.title,
          //     toastLength: Toast.LENGTH_SHORT,
          //     gravity: ToastGravity.CENTER,
          //     timeInSecForIosWeb: 1,
          //     backgroundColor: Colors.red,
          //     textColor: Colors.white,
          //     fontSize: 16.0);
          // DeBugLog.log(value.title);
          //导航到新路由
          Navigator.push(
            context,
            MaterialPageRoute(builder: (context) {
              return WebViewExample(title: value.title, url: value.link);
            }),
          );
        },
      ));
    }
    return list;
  }
}

class MsgPage extends StatelessWidget {
  const MsgPage({Key? key}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Container(color: Colors.red, child: const Text('Hello!')),
            Container(color: Colors.green, child: const Text('Goodbye!')),
          ],
        ),
        Wrap(
          children: [Text("xxx" * 100)],
        ),
        ClipRRect(
            borderRadius: BorderRadius.all(Radius.circular(10)),
            child: Container(
              width: 100,
              height: 100,
              color: Colors.lightBlue,
            )),
        ClipOval(
            child: Container(
              width: 100,
              height: 100,
              color: Colors.lightBlue,
            )),
        wContainer(BoxFit.none),
        Text('Wendux'),
        wContainer(BoxFit.contain),
        Text('Flutter中国'),
      ],
    );
  }

  Widget wContainer(BoxFit boxFit) {
    return Container(
      width: 50,
      height: 50,
      color: Colors.red,
      child: FittedBox(
        fit: boxFit,
        // 子容器超过父容器大小
        child: Container(width: 60, height: 70, color: Colors.blue),
      ),
    );
  }
}

class CartPage extends StatelessWidget {
  const CartPage({Key? key}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  @override
  Widget build(BuildContext context) {
    return Column(
      //测试Row对齐方式，排除Column默认居中对齐的干扰
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(" hello world "),
            Text(" I am Jack "),
          ],
        ),
        Row(
          mainAxisSize: MainAxisSize.min,
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(" hello world "),
            Text(" I am Jack "),
          ],
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.end,
          textDirection: TextDirection.rtl,
          children: <Widget>[
            Text(" hello world "),
            Text(" I am Jack "),
          ],
        ),
        Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          verticalDirection: VerticalDirection.up,
          children: <Widget>[
            Text(
              " hello world ",
              style: TextStyle(fontSize: 30.0),
            ),
            Text(" I am Jack "),
          ],
        ),
      ],
    );
  }
}

class PersonPage extends StatelessWidget {
  const PersonPage({Key? key}) : super(key: key);

  // This widget is the home page of your application. It is stateful, meaning
  // that it has a State object (defined below) that contains fields that affect
  // how it looks.

  // This class is the configuration for the state. It holds the values (in this
  // case the title) provided by the parent (in this case the App widget) and
  // used by the build method of the State. Fields in a Widget subclass are
  // always marked "final".

  @override
  Widget build(BuildContext context) {
    return Stack(children: [Text("PersonPage"), Text("hhhhh")]);
  }
}
