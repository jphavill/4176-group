package com.example.csci4176_groupproject

import android.app.AlertDialog
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView

class Store : AppCompatActivity() {

    private lateinit var settingPrefs: SharedPreferences
    private lateinit var starsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        // Get the reference to the stars text view
        starsTextView = findViewById(R.id.starsTextView)

        // Get the reference to the shared preferences object
        settingPrefs = applicationContext.getSharedPreferences("settingsPrefs", 0)

        // Load the number of stars from the shared preferences and display it in the text view
        val totalStars = settingPrefs.getInt("stars", 0)
        starsTextView.text = "Stars: $totalStars"

        // Create the list of items to display in the store
        val itemList = mutableListOf(
            Model("White Ball", "Default Skin", R.drawable.whiteball, 0),
            Model("Red Ball", "Normal Skin", R.drawable.redball, 3),
            Model("Blue Ball", "Normal Skin", R.drawable.blueball, 3),
            Model("Devil Ball", "Unique Skin", R.drawable.devil, 7),
            Model("Sun Ball", "Unique Skin", R.drawable.sun, 7),
        )

        // Create an adapter to display the list of items in a list view
        val adapter = MyAdapter(this, R.layout.row, itemList)
        val listView = findViewById<ListView>(R.id.listView)
        listView.adapter = adapter

        // Set up the click listener for the list view items
        listView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = itemList[position]
            val cost = selectedItem.cost

            // Check if the player has enough stars to purchase the selected item
            val totalStars = settingPrefs.getInt("stars", 0)
            if (cost > 0 && totalStars >= cost) {
                // Show a confirmation dialog to the player
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Purchase Item")
                builder.setMessage("Are you sure you want to purchase ${selectedItem.title} for $cost stars?")
                builder.setPositiveButton("Yes") { _, _ ->
                    // Subtract the cost of the item from the total number of stars
                    val editor = settingPrefs.edit()
                    editor.putInt("stars", totalStars - cost)
                    editor.apply()

                    // TODO: Add code to apply the selected item

                    // Update the UI to reflect the new number of stars
                    starsTextView.text = "Stars: ${totalStars - cost}"
                }
                builder.setNegativeButton("No") { _, _ ->
                    // Do nothing
                }
                builder.show()
            } else {
                // Show an error message to the player
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Not Enough Stars")
                builder.setMessage("You do not have enough stars to purchase ${selectedItem.title}.")
                builder.setPositiveButton("OK") { _, _ ->
                    // Do nothing
                }
                builder.show()
            }
        }
    }
}