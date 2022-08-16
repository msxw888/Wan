
import 'package:dio/dio.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

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

class _HomePageState extends State<HomePage>{

  late ArticalEntity? netData = null;


  @override
  void initState() {
    getHttp();
  }

  @override
  Widget build(BuildContext context) {
    return Wrap(children: _getList());
  }

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
          // print(value.title);
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
    return Text("MsgPage");
    
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
    return Text("CartPage");

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
    return Text("PersonPage");

  }
}
