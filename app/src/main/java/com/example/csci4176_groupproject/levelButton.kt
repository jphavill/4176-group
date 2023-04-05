package com.example.csci4176_groupproject

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.csci4176_groupproject.databinding.FragmentLevelButtonBinding
import com.example.csci4176_groupproject.levels.levelActivities
import com.google.gson.Gson


class levelButton: Fragment(), binaryDialogCallback {
    private var _binding: FragmentLevelButtonBinding? = null
    private val binding get() = _binding!!
    var buttonIndex: Int = 0
    lateinit var levels: List<Class<out AppCompatActivity>>
    var visible: Boolean = true
    // first 5 levels are unlocked by default
    lateinit var level: levelData
    lateinit var settingPrefs: SharedPreferences
    private val starCount: StarCountViewModel by activityViewModels()


    override fun binaryDialogCallback(result: Boolean){
        if (result){
            starCount.setCount(1)
            update()
        }
    }

    private fun update() {
        val button = binding.button
        val gson = Gson()
        if (visible){
            button.visibility = View.VISIBLE
            button.isEnabled = true
            button.isClickable = true


            level = gson.fromJson(settingPrefs.getString(String.format("level%d", buttonIndex+1), gson.toJson(level)), levelData::class.java)
        } else {
            button.visibility = View.INVISIBLE
            button.isEnabled = false
            button.isClickable = false
        }

        updateStars()

        val displayIndex = buttonIndex+1
        val intentIndex = buttonIndex
        if (level.locked){
            button.text = ""
            button.setBackgroundResource(R.drawable.lock)
            button.background.setTint(ContextCompat.getColor(requireContext(), R.color.black))
            button.setOnClickListener {
                unlockLevel(level.id)
                update()
            }
        } else {
            button.text = String.format("Level %d", displayIndex)
            button.setBackgroundResource(android.R.drawable.btn_default)
            if (level.tried){
                if (level.time < 0){
                    button.background.setTintList(ContextCompat.getColorStateList(requireContext(), R.color.red))
                } else {
                    button.background.setTintList(ContextCompat.getColorStateList(requireContext(), R.color.green))
                }
            } else {
                button.background.setTintList(ContextCompat.getColorStateList(requireContext(), R.color.light_grey))
            }
            button.setOnClickListener {
                level.tried = true
                val editor: SharedPreferences.Editor = settingPrefs.edit()
                editor.putString(String.format("level%d", level.id), gson.toJson(level))
                editor.apply()
                val intent = Intent(context, levels[intentIndex])
                startActivity(intent)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        buttonIndex = requireArguments().getInt("buttonIndex")
        levels = levelActivities().levels
        visible = buttonIndex < levels.size
        // first 5 levels are unlocked by default
        level = levelData(id=buttonIndex, locked=buttonIndex<6)

        _binding = FragmentLevelButtonBinding.inflate(inflater, container, false)
        settingPrefs = requireContext().applicationContext.getSharedPreferences("settingsPrefs", 0)

        update()
        return binding.root
    }

    private fun updateStars() {
        val stars = listOf<ImageView>(binding.star1, binding.star2, binding.star3)
        var starIndex = 0
        for (s in stars){
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

    private fun unlockLevel(levelId: Int){
        unlockDialog(context = requireContext()).showUnlock(levelId, this)
    }
}