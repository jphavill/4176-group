//Contributors: Jason Havill
package com.example.csci4176_groupproject.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.csci4176_groupproject.activities.LevelSelectActivity
import com.example.csci4176_groupproject.activities.MainMenuActivity
import com.example.csci4176_groupproject.databinding.FragmentTopBarBinding
import com.example.csci4176_groupproject.dialogs.SettingsDialog
import com.example.csci4176_groupproject.interfaces.SettingsDialogCallback
import com.example.csci4176_groupproject.models.SettingChange
import com.example.csci4176_groupproject.models.Settings
import com.example.csci4176_groupproject.viewModels.SettingsViewModel

class TopBarFragment : Fragment(), SettingsDialogCallback {
    // the Back Button, Title, and Settings button at the top of all activities
    // except the main menu
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
        // back button can either lead to level select activity or main menu
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

    override fun settingsDialogCallback(settings: Settings) {
        // for each setting that changes, send out and update on the corresponding observable
        val changes = settings.changes
        if (changes[SettingChange.ColourBlindMode]!!) {
            settingsViewModel.setColorBlindMode(settings.colourBlindMode)
        }
        if (changes[SettingChange.PlayerSkin]!!) {
            settingsViewModel.setPlayerSkin(settings.playerSkin)
        }
        if (changes[SettingChange.Haptics]!!) {
            settingsViewModel.setHaptics(settings.haptics)
        }
        if (changes[SettingChange.ResetLevels]!!) {
            settingsViewModel.setResetLevels(settings.resetLevels)
        }
        if (changes[SettingChange.ResetStore]!!) {
            settingsViewModel.setResetStore(settings.resetStore)
        }

    }


}