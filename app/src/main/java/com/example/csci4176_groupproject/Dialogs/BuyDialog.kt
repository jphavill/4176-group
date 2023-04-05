package com.example.csci4176_groupproject.Dialogs

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.csci4176_groupproject.Buyable
import com.example.csci4176_groupproject.R

class BuyDialog(context: Context) : AlertDialog.Builder(context) {
    private val settingPrefs: SharedPreferences = context.applicationContext.getSharedPreferences("settingsPrefs", 0)

    /**
     * @param milliseconds the number of miliseconds it took a user to complete a level
     * @param starTime the time in miliseconds a user must beat to earn the first star
     */
    fun showBuy(item: Buyable, callback: BuyDialogCallback){
        val builder = AlertDialog.Builder(context, R.style.SettingsDialog).create()
        val li = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.buy_dialog, null)
        builder.setView(view)

        var totalStars = settingPrefs.getInt("stars", 0)
        val cost = item.cost
        val title = item.title
        val s = if (cost > 1) "s" else ""

        val unlockButton = view.findViewById<Button>(R.id.unlockButton)
        val purchaseText = view.findViewById<TextView>(R.id.purchaseText)

        if (cost <= totalStars){
            unlockButton.setOnClickListener {
                val editor: SharedPreferences.Editor = settingPrefs.edit()
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
            builder.cancel()
            callback.binaryDialogCallback(false)
        }

        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }
}