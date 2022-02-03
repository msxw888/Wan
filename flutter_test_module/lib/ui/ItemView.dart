

import 'package:flutter/cupertino.dart';

class ItemView extends StatefulWidget {
  const ItemView({Key? key}) : super(key: key);

  @override
  _ItemViewState createState() => _ItemViewState();
}

class _ItemViewState extends State<ItemView> {
  @override
  Widget build(BuildContext context) {
    return Container(
      child: Row(
        children: const [
          Text("test"),
          Text("dart笔记"),
          Text("自主"),
        ],
      ),
    );
  }
}
