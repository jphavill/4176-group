package com.example.csci4176_groupproject

import android.widget.PopupWindow
import android.widget.Spinner

// Solution for hiding the android status bar at the top of the screen while the menu of the spinner is open
// Taken from kakajika's github https://gist.github.com/kakajika/a236ba721a5c0ad3c1446e16a7423a63
fun Spinner.avoidDropdownFocus() {
    try {
        val isAppCompat = this is androidx.appcompat.widget.AppCompatSpinner
        val spinnerClass =
            if (isAppCompat) androidx.appcompat.widget.AppCompatSpinner::class.java else Spinner::class.java
        val popupWindowClass =
            if (isAppCompat) androidx.appcompat.widget.ListPopupWindow::class.java else android.widget.ListPopupWindow::class.java

        val listPopup = spinnerClass
            .getDeclaredField("mPopup")
            .apply { isAccessible = true }
            .get(this)
        if (popupWindowClass.isInstance(listPopup)) {
            val popup = popupWindowClass
                .getDeclaredField("mPopup")
                .apply { isAccessible = true }
                .get(listPopup)
            if (popup is PopupWindow) {
                popup.isFocusable = false
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}