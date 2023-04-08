//Contributors: Jason Havill
package com.example.csci4176_groupproject.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.data.LevelActivities
import com.example.csci4176_groupproject.databinding.FragmentLevelButtonBinding
import com.example.csci4176_groupproject.dialogs.BuyDialog
import com.example.csci4176_groupproject.interfaces.BuyDialogCallback
import com.example.csci4176_groupproject.models.Level
import com.example.csci4176_groupproject.viewModels.StarCountViewModel
import com.google.gson.Gson


class LevelButtonFragment : Fragment(), BuyDialogCallback {
    private var _binding: FragmentLevelButtonBinding? = null
    private val binding get() = _binding!!
    private var buttonIndex: Int = 0
    lateinit var levels: List<Class<out AppCompatActivity>>
    private var visible: Boolean = true
    private lateinit var level: Level
    lateinit var settingPrefs: SharedPreferences
    private val starCount: StarCountViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        buttonIndex = requireArguments().getInt("buttonIndex")
        levels = LevelActivities().levels
        // if the button is on the last page, and is greater than the number of levels, then hide it
        // for example button 11 when there are only 10 levels is hidden
        visible = buttonIndex < levels.size
        // first 5 levels are unlocked by default
        level = Level(id = buttonIndex, locked = buttonIndex < 6)

        _binding = FragmentLevelButtonBinding.inflate(inflater, container, false)
        settingPrefs = requireContext().applicationContext.getSharedPreferences("settingsPrefs", 0)

        update()
        return binding.root
    }

    override fun buyDialogCallback(result: Boolean) {
        // if the user did unlock the level in the dailog then update the star count and unlock
        if (result) {
            unlock()
            starCount.setCount(1)
            update()
        }
    }

    private fun unlock() {
        // store the unlocked state of the level persistently
        val gson = Gson()
        val editor: SharedPreferences.Editor = settingPrefs.edit()
        level.locked = false
        editor.putString(String.format("level%d", level.id), gson.toJson(level))
        editor.apply()
    }

    private fun update() {
        val button = binding.button
        val gson = Gson()
        if (visible) {
            button.visibility = View.VISIBLE
            button.isEnabled = true
            button.isClickable = true

            // if the button is visible then there is a corresponding level
            // retrieve it from persistent memory
            level = gson.fromJson(
                settingPrefs.getString(
                    String.format("level%d", buttonIndex + 1),
                    gson.toJson(level)
                ), Level::class.java
            )
        } else {
            button.visibility = View.INVISIBLE
            button.isEnabled = false
            button.isClickable = false
        }

        updateStars()

        // button index's are 0, while levels are indexed at 1. For example button 0 = level 1
        val displayIndex = buttonIndex + 1
        if (level.locked) {
            // if the level is locked, it will open an unlock dialog on click
            button.text = ""
            button.setBackgroundResource(R.drawable.lock)
            button.background.setTint(ContextCompat.getColor(requireContext(), R.color.black))
            button.setOnClickListener {
                unlockLevel()
                update()
            }
        } else {
            button.text = String.format("Level %d", displayIndex)
            button.setBackgroundResource(android.R.drawable.btn_default)
            if (level.tried) {
                if (level.time < 0) {
                    // if a user has tried a level, but the time is below 0 then the user has not
                    // successfully completed the level (the time is still at it's default)
                    // therefore make it red to indicate failure
                    button.background.setTintList(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.red
                        )
                    )
                } else {
                    // if the user has tried a level and the level has a time above 0 then the user
                    // has successfully completed a level. Make it green to indicate success
                    button.background.setTintList(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.green
                        )
                    )
                }
            } else {
                // if a user has not tried a level make it grey
                button.background.setTintList(
                    ContextCompat.getColorStateList(
                        requireContext(),
                        R.color.light_grey
                    )
                )
            }
            button.setOnClickListener {
                // when a level is opened, set tried to true. This is used to determine the color
                // of the the level in the level select screen.
                level.tried = true
                val editor: SharedPreferences.Editor = settingPrefs.edit()
                editor.putString(String.format("level%d", level.id), gson.toJson(level))
                editor.apply()
                val intent = Intent(context, levels[buttonIndex])
                startActivity(intent)
            }
        }
    }

    private fun updateStars() {
        val stars = listOf(binding.star1, binding.star2, binding.star3)
        var starIndex = 0
        for (s in stars) {
            // if a button is invisible or locked, hide the stars
            // otherwise turn on the number of stars the user has earned for that level
            if (visible && starIndex < level.starsEarned) {
                s.visibility = View.VISIBLE
                s.setImageResource(android.R.drawable.btn_star_big_on)
            } else if (visible && !level.locked) {
                s.visibility = View.VISIBLE
                s.setImageResource(android.R.drawable.btn_star_big_off)
            } else {
                s.visibility = View.INVISIBLE
            }
            starIndex += 1
        }
    }

    private fun unlockLevel() {
        BuyDialog(context = requireContext()).showBuy(level, this)
    }
}