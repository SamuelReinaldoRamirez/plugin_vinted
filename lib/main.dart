import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: OverlayApp(),
    );
  }
}

class OverlayApp extends StatelessWidget {
  static const platform = MethodChannel('overlay_channel');

  void showOverlay() async {
    try {
      await platform.invokeMethod('showOverlay');
    } on PlatformException catch (e) {
      print("Erreur lors de l'affichage de l'overlay : ${e.message}");
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Overlay App")),
      body: Center(
        child: ElevatedButton(
          onPressed: showOverlay,
          child: Text("Afficher le rond rouge"),
        ),
      ),
    );
  }
}
