package com.example.csci4176_groupproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.csci4176_groupproject.R
import com.example.csci4176_groupproject.data.LevelActivities
import com.example.csci4176_groupproject.databinding.FragmentLevelButtonBinding
import com.example.csci4176_groupproject.databinding.FragmentRestartButtonBinding
import com.example.csci4176_groupproject.models.Level
import com.example.csci4176_groupproject.viewModel.RestartLevelViewModel
import com.example.csci4176_groupproject.viewModel.StarCountViewModel


class RestartButtonFragment : Fragment() {
    private var _binding: FragmentRestartButtonBinding? = null
    private val binding get() = _binding!!
    private val restartLevel: RestartLevelViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestartButtonBinding.inflate(inflater, container, false)

        binding.resetLevelsButton.setOnClickListener{
            restartLevel.setRestartLevel(true)
        }

        return binding.root
    }

}