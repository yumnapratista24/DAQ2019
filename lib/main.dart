import 'package:flutter/material.dart';
import 'package:english_words/english_words.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Startup name generator',
      home: TombolWords(),
    );
  }
}

class TombolWords extends StatefulWidget{
  @override
  _tombolWords createState() => _tombolWords();
}

class _tombolWords extends State<TombolWords> {
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return AlertDialog(
//        appBar: AppBar(
//          title: Text('ANAS MAHASIN NABIH'),
//        ),
      titlePadding: EdgeInsets.all(0),
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.all(Radius.circular(10)),
      ),
      title: Container(
        decoration: BoxDecoration(
          color: Color(0xFF16A613),
          borderRadius: BorderRadius.vertical(
            top: Radius.circular(10)
          )
        ),
        padding: EdgeInsets.symmetric(
          vertical: 10
        ),
        child: Center(
          child: Text(
            "Ayam",
            style: TextStyle(
              color: Colors.white,
              fontSize: 50,
              fontFamily: "Lato",
            ),
          ),
        )
      ),
      content: Container(
        height: 200,
        child: Column(
          children: <Widget>[
            Text("Yumna boker kagak mandi"),
            _answerSheet(),
          ],
        ),
      ),
      actions: <Widget>[
        _muteButton(),
      ],
    );
  }

  Widget _muteButton(){
    return Container(
      height: 50,
      child: Text("MUTE BUTTON")
    );
  }
  Widget _answerSheet() {
    return Container(
      height: 150,
      child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            _rowTombol(),
            _rowTombol()
          ]
      )
    );
  }
  Widget _rowTombol() {
    return Row(
      mainAxisAlignment: MainAxisAlignment.spaceEvenly,
      children: <Widget>[
        _tombol(),
        _tombol(),
      ],
    );
  }
  Widget _tombol() {
    return RaisedButton(
      child: Text("BAYAM"),
      onPressed: () {
        print("Botak");
      },
    );
  }
//
//  Widget _buildColumn() {
//    return Center(
//      child: Column(
//        children: <Widget>[
//          _tombol(),
//          _tombol(),
//        ]
//      )
//    );
//  }

//  Widget _buildRow() {
//    return Container(
//        child: Center(
//            child: _()
//        )
//    );
//  }
//}
}