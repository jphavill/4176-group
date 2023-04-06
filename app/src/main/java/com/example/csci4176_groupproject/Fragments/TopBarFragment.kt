package com.example.csci4176_groupproject.Fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.csci4176_groupproject.Activities.LevelSelectActivity
import com.example.csci4176_groupproject.Activities.MainMenuActivity
import com.example.csci4176_groupproject.Dialogs.SettingsDialog
import com.example.csci4176_groupproject.Dialogs.SettingsDialogCallback
import com.example.csci4176_groupproject.Models.Settings
import com.example.csci4176_groupproject.SettingsViewModel
import com.example.csci4176_groupproject.databinding.FragmentTopBarBinding

class TopBarFragment : Fragment(), SettingsDialogCallback {
    private var _binding: FragmentTopBarBinding? = null
    private val binding get() = _binding!!
    lateinit var settingPrefs: SharedPreferences
    private val settingsViewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTopBarBinding.inflate(inflater, container, false)
        settingPrefs = requireContext().applicationContext.getSharedPreferences("settingsPrefs", 0)
        val backActivity = requireArguments().getString("backActivity").toString()
        val title = requireArguments().getString("title").toString()
        binding.TitleTextView.text = title

        val backToLevelSelectButton = binding.BackToHomeButton
        backToLevelSelectButton.setOnClickListener {
            val intent = if (backActivity == "LevelSelectActivity") {
                Intent(context, LevelSelectActivity::class.java)
            } else {
                Intent(context, MainMenuActivity::class.java)
            }

            startActivity(intent)
        }

        val settingsButton = binding.SettingsButton
        settingsButton.setOnClickListener {
            SettingsDialog(context = requireContext()).showSettings(this)
        }

        return binding.root
    }

    override fun settingsDialogCallback(settings: Settings){
        val changes = settings.changes
        if (changes["colourBlindMode"]!!){
            settingsViewModel.setColorBlindMode(settings.colourBlindMode)
        }
        if (changes["playerSkin"]!!){
            settingsViewModel.setPlayerSkin(settings.playerSkin)
        }
        if (changes["haptics"]!!){
            settingsViewModel.setHaptics(settings.haptics)
        }

    }


}