package com.example.csci4176_groupproject.fragments

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.csci4176_groupproject.data.CosmeticList
import com.example.csci4176_groupproject.databinding.FragmentCosmeticButtonBinding
import com.example.csci4176_groupproject.dialogs.BuyDialog
import com.example.csci4176_groupproject.interfaces.BuyDialogCallback
import com.example.csci4176_groupproject.models.Cosmetic
import com.example.csci4176_groupproject.viewModels.StarCountViewModel
import com.google.gson.Gson


class CosmeticButtonFragment : Fragment(), BuyDialogCallback {
    private var _binding: FragmentCosmeticButtonBinding? = null
    private val binding get() = _binding!!
    private var cosmeticId: Int = 0
    private lateinit var cosmetics: List<Cosmetic>

    // first 5 levels are unlocked by default
    private lateinit var cosmetic: Cosmetic
    lateinit var settingPrefs: SharedPreferences
    private val starCount: StarCountViewModel by activityViewModels()


    override fun binaryDialogCallback(result: Boolean) {
        if (result) {
            starCount.setCount(1)
            unlock()
            update()
        }
    }

    private fun unlock() {
        val gson = Gson()
        val editor: SharedPreferences.Editor = settingPrefs.edit()
        cosmetic.locked = false
        cosmetic.img = CosmeticList().skinList[cosmeticId]
        editor.putString(String.format("cosmetic%d", cosmeticId), gson.toJson(cosmetic))
        editor.apply()
    }

    private fun update() {
        val gson = Gson()
        cosmetic = gson.fromJson(
            settingPrefs.getString(
                String.format("cosmetic%d", cosmeticId),
                gson.toJson(cosmetic)
            ), Cosmetic::class.java
        )
        binding.description.text = if (cosmetic.locked) cosmetic.description else "Sold"
        binding.title.text = cosmetic.title
        val button = binding.cosmeticImage
        button.setBackgroundResource(cosmetic.img)
        button.text = ""
        if (cosmetic.locked) {
            button.isClickable = true
            button.setOnClickListener {
                unlockCosmetic()
                update()
            }
        } else {
            button.isClickable = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cosmeticId = requireArguments().getInt("cosmeticId")
        cosmetics = CosmeticList().itemList
        cosmetic = cosmetics[cosmeticId]

        _binding = FragmentCosmeticButtonBinding.inflate(inflater, container, false)
        settingPrefs = requireContext().applicationContext.getSharedPreferences("settingsPrefs", 0)

        update()
        return binding.root
    }

    private fun unlockCosmetic() {
        BuyDialog(context = requireContext()).showBuy(cosmetic, this)
    }
}