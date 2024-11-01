// package com.example.plugin_vinted

// import android.app.Service
// import android.content.Intent
// import android.graphics.PixelFormat
// import android.os.IBinder
// import android.view.Gravity
// import android.view.LayoutInflater
// import android.view.MotionEvent
// import android.view.View
// import android.view.WindowManager

// class OverlayService : Service() {
//     private lateinit var windowManager: WindowManager
//     private lateinit var overlayView: View
//     private var initialX = 0
//     private var initialY = 0
//     private var initialTouchX = 0f
//     private var initialTouchY = 0f

//     override fun onCreate() {
//         super.onCreate()
//         windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

//         // Créer une vue pour le cercle rouge
//         overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null)

//         // Paramètres de la fenêtre en overlay
//         val params = WindowManager.LayoutParams(
//             WindowManager.LayoutParams.WRAP_CONTENT,
//             WindowManager.LayoutParams.WRAP_CONTENT,
//             WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
//             WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
//             PixelFormat.TRANSLUCENT
//         )
//         params.gravity = Gravity.CENTER

//         // Ajouter la vue en overlay
//         windowManager.addView(overlayView, params)

//         // Gérer le toucher pour le mouvement de l'overlay
//         overlayView.setOnTouchListener { _, event ->
//             when (event.action) {
//                 MotionEvent.ACTION_DOWN -> {
//                     // Stocker la position initiale de l'overlay
//                     initialX = params.x
//                     initialY = params.y
//                     initialTouchX = event.rawX
//                     initialTouchY = event.rawY
//                     true
//                 }
//                 MotionEvent.ACTION_MOVE -> {
//                     // Calculer la nouvelle position de l'overlay
//                     params.x = initialX + (event.rawX - initialTouchX).toInt()
//                     params.y = initialY + (event.rawY - initialTouchY).toInt()
//                     // Mettre à jour la position de l'overlay
//                     windowManager.updateViewLayout(overlayView, params)
//                     true
//                 }
//                 else -> false
//             }
//         }
//     }

//     override fun onDestroy() {
//         super.onDestroy()
//         windowManager.removeView(overlayView)
//     }

//     override fun onBind(intent: Intent?): IBinder? = null
// }





package com.example.plugin_vinted

import android.app.ActivityManager
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.content.Context // Ajoutez cette importation
import android.util.Log // Ajoutez cette importation

class OverlayService : Service() {
    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View
    private lateinit var statusText: TextView
    private var initialX = 0
    private var initialY = 0
    private var initialTouchX = 0f
    private var initialTouchY = 0f

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        // Créer une vue pour le cercle rouge
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null)

        // Initialiser le TextView
        statusText = overlayView.findViewById(R.id.statusText)

        // Mettre à jour le texte selon l'état de l'application Vinted
        updateStatusText()

        // Paramètres de la fenêtre en overlay
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.CENTER

        // Ajouter la vue en overlay
        windowManager.addView(overlayView, params)

        // Gérer le toucher pour le mouvement de l'overlay
        overlayView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialX = params.x
                    initialY = params.y
                    initialTouchX = event.rawX
                    initialTouchY = event.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    params.x = initialX + (event.rawX - initialTouchX).toInt()
                    params.y = initialY + (event.rawY - initialTouchY).toInt()
                    windowManager.updateViewLayout(overlayView, params)
                    true
                }
                else -> false
            }
        }
    }

    private fun updateStatusText() {
        val text = if (isVintedOpen()) "OK" else "KO"
        statusText.text = text
    }

    // private fun isVintedOpen(): Boolean {
    //     val packageName = "com.vinted" // Remplacez par le nom de package correct de l'application Vinted
    //     val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
    //     val runningApps = activityManager.runningAppProcesses
    //     for (process in runningApps) {
    //         if (process.processName == packageName) {
    //             return true
    //         }
    //     }
    //     return false
    // }

    private fun isVintedOpen(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = activityManager.runningAppProcesses
        val packageNameVinted = "com.vinted"

        // Imprimez tous les noms des applications ouvertes
        runningApps.forEach { appProcess ->
            Log.d("OverlayService", "Application ouverte: ${appProcess.processName}")
        }

        // Vérifiez si Vinted est parmi les applications ouvertes
        return runningApps.any { it.processName == packageNameVinted }
    }

    override fun onDestroy() {
        super.onDestroy()
        windowManager.removeView(overlayView)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
