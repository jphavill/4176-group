package com.example.csci4176_groupproject.dialogs

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.interfaces.BuyDialogCallback
import com.example.csci4176_groupproject.interfaces.Buyable

class BuyDialog(context: Context) : AlertDialog.Builder(context) {
    private val settingPrefs: SharedPreferences =
        context.applicationContext.getSharedPreferences("settingsPrefs", 0)

    fun showBuy(item: Buyable, callback: BuyDialogCallback) {
        val builder = AlertDialog.Builder(context, R.style.roundedDialog).create()
        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.buy_dialog, null)
        view.isHapticFeedbackEnabled = settingPrefs.getBoolean("haptics", true)
        builder.setView(view)

        val totalStars = settingPrefs.getInt("stars", 0)
        val cost = item.cost
        val title = item.title
        val s = if (cost > 1) "s" else ""

        val unlockButton = view.findViewById<Button>(R.id.unlockButton)
        val purchaseText = view.findViewById<TextView>(R.id.purchaseText)

        if (cost <= totalStars) {
            unlockButton.setOnClickListener {
                val editor: SharedPreferences.Editor = settingPrefs.edit()
                view.performHapticFeedback(16)
                editor.putInt("stars", totalStars - cost)
                editor.apply()
                callback.binaryDialogCallback(true)
                builder.dismiss()
            }
            purchaseText.text = String.format("Unlock %s for %d Star%s?", title, cost, s)
        } else {
            unlockButton.visibility = View.GONE
            purchaseText.text = String.format("Not enough Stars. Costs %d Star%s.", cost, s)
        }
        val cancelButton = view.findViewById<Button>(R.id.cancelPurchase)
        cancelButton.setOnClickListener {
            // if the cancel button is hit, don't save settings and exit
            view.performHapticFeedback(17)
            builder.cancel()
            callback.binaryDialogCallback(false)
        }

        builder.setCanceledOnTouchOutside(false)
        builder.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )
        builder.show()
    }
}