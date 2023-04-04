package com.example.csci4176_groupproject

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import android.widget.ImageButton


class Store : AppCompatActivity() {

    private lateinit var settingPrefs: SharedPreferences
    private lateinit var starsTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        //Set the back button to go back to main menu
        val backButton = findViewById<ImageButton>(R.id.BackToHomeButton)
        backButton.setOnClickListener {
            val intent = Intent(this, FullscreenActivity::class.java)
            startActivity(intent)
        }

        // Get the reference to the stars text view
        starsTextView = findViewById(R.id.starCount)

        // Get the reference to the shared preferences object
        settingPrefs = applicationContext.getSharedPreferences("settingsPrefs", 0)

        // Load the number of stars from the shared preferences and display it in the text view
        val totalStars = settingPrefs.getInt("stars", 0)
        starsTextView.text = "$totalStars"

        // Create the list of items to display in the store
        val itemList = mutableListOf(

            Model("Red Ball", "Normal Skin (Cost:3)", R.drawable.locked_redball, 3),
            Model("Blue Ball", "Normal Skin (Cost:3)", R.drawable.locked_blueball, 3),
            Model("Devil Ball", "Unique Skin (Cost:7)", R.drawable.locked_devilball, 7),
            Model("Sun Ball", "Unique Skin (Cost:7)", R.drawable.locked_sunball, 7),
        )

//        // Create an adapter to display the list of items in a list view
//        val adapter = MyAdapter(this, R.layout.row, itemList)
//        val listView = findViewById<ListView>(R.id.listView)
//        listView.adapter = adapter
//        // Get the reference to the shared preferences object for skins
//        val skinPrefs = applicationContext.getSharedPreferences("skinPrefs", 0)
//        // Load the state of each skin from the shared preferences and update the model objects
//        for (item in itemList) {
//            val isSold = skinPrefs.getBoolean(item.title, false)
//            if (isSold) {
//                item.isSold = true
//                item.description = "Sold"
//                // Update the image resource ID based on the purchased skin
//                when (item.img) {
//                    R.drawable.locked_redball -> item.img = R.drawable.redball
//                    R.drawable.locked_blueball -> item.img = R.drawable.blueball
//                    R.drawable.locked_devilball -> item.img = R.drawable.devil
//                    R.drawable.locked_sunball -> item.img = R.drawable.sun
//                }
//            }
//        }

//        // Set up the click listener for the list view items
//        listView.setOnItemClickListener { parent, view, position, id ->
//            val selectedItem = itemList[position]
//            val cost = selectedItem.cost
//            // Check if the item has already been sold
//            if (selectedItem.isSold) {
//                // Show a message to the player indicating that the item has already been sold
//                val builder = AlertDialog.Builder(this)
//                builder.setTitle("Item Already Purchased")
//                builder.setMessage("This is the skin item you already have purchased.")
//                builder.setPositiveButton("OK") { _, _ ->
//                    // Do nothing
//                }
//                builder.show()
//            } else {
//                // Check if the player has enough stars to purchase the selected item
//
//                val totalStars = settingPrefs.getInt("stars", 0)
//                if (cost >= 0 && totalStars >= cost) {
//                    // Show a confirmation dialog to the player
//                    val builder = AlertDialog.Builder(this)
//                    builder.setTitle("Purchase Item")
//                    builder.setMessage("Are you sure you want to purchase ${selectedItem.title} for $cost stars?")
//                    builder.setPositiveButton("Yes") { _, _ ->
//                        // Subtract the cost of the item from the total number of stars
//                        val editor = settingPrefs.edit()
//                        editor.putInt("stars", totalStars - cost)
//                        editor.apply()
//
//
//
//                        // Apply the selected item by updating its image resource ID
//                        when (selectedItem.img) {
//                            R.drawable.locked_redball -> selectedItem.img = R.drawable.redball
//                            R.drawable.locked_blueball -> selectedItem.img = R.drawable.blueball
//                            R.drawable.locked_devilball -> selectedItem.img = R.drawable.devil
//                            R.drawable.locked_sunball -> selectedItem.img = R.drawable.sun
//                        }
//
//                        // Update the item description to indicate that it's been sold
//                        selectedItem.isSold = true
//                        selectedItem.description = "Sold"
//
//
//                        // Update the UI to reflect the new number of stars
//                        starsTextView.text = "${totalStars - cost}"
//
//                        for (item in itemList) {
//                            val isSold = skinPrefs.getBoolean(item.title, false)
//                            if (isSold) {
//                                item.isSold = true
//                                item.description = "Sold"
//                                // Update the image resource ID based on the purchased skin
//                                when (item.img) {
//                                    R.drawable.locked_redball -> item.img = R.drawable.redball
//                                    R.drawable.locked_blueball -> item.img = R.drawable.blueball
//                                    R.drawable.locked_devilball -> item.img = R.drawable.devil
//                                    R.drawable.locked_sunball -> item.img = R.drawable.sun
//                                }
//                            }
//                            // Save the updated state for the skin item
//                            skinPrefs.edit().putBoolean(item.title, item.isSold).apply()
//                        }
//
//
//                        //save the newly updated adapter
//                        adapter.notifyDataSetChanged()
//                    }
//                    builder.setNegativeButton("No") { _, _ ->
//                        // Do nothing
//                    }
//                    builder.show()
//                } else {
//                    // Show an error message to the player
//                    val builder = AlertDialog.Builder(this)
//                    builder.setTitle("Not Enough Stars")
//                    builder.setMessage("You do not have enough stars to purchase ${selectedItem.title}.")
//                    builder.setPositiveButton("OK") { _, _ ->
//                        // Do nothing
//                    }
//                    builder.show()
//                }
//            }
//
//        }

    }
}





