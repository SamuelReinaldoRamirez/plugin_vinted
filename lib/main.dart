// import 'package:flutter/material.dart';
// import 'package:flutter/services.dart';

// void main() => runApp(MyApp());

// class MyApp extends StatelessWidget {
//   @override
//   Widget build(BuildContext context) {
//     return MaterialApp(
//       home: OverlayApp(),
//     );
//   }
// }

// class OverlayApp extends StatelessWidget {
//   static const platform = MethodChannel('overlay_channel');

//   void showOverlay() async {
//     try {
//       await platform.invokeMethod('showOverlay');
//     } on PlatformException catch (e) {
//       print("Erreur lors de l'affichage de l'overlay : ${e.message}");
//     }
//   }

//   @override
//   Widget build(BuildContext context) {
//     return Scaffold(
//       appBar: AppBar(title: Text("Overlay App")),
//       body: Center(
//         child: ElevatedButton(
//           onPressed: showOverlay,
//           child: Text("Afficher le rond rouge"),
//         ),
//       ),
//     );
//   }
// }

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:permission_handler/permission_handler.dart';

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

  // Fonction pour demander la permission d'overlay
  Future<void> _requestOverlayPermission() async {
    // Vérifie si la permission d'affichage en overlay est accordée
    if (await Permission.systemAlertWindow.isGranted) {
      return; // La permission est déjà accordée
    }

    // Si la permission n'est pas accordée, demande-la
    await Permission.systemAlertWindow.request();
  }

  void showOverlay() async {
    await _requestOverlayPermission(); // Demander la permission avant d'afficher l'overlay
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
