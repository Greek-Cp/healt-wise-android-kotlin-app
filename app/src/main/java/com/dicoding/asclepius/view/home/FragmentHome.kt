package com.dicoding.asclepius.view.home

import SettingAppPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.dicoding.asclepius.R
import dataStore
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.dicoding.asclepius.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.first

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentHome.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentHome : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentHomeBinding? = null
    private val binding get()= _binding!!
    private val settingPreferences: SettingAppPreferences by lazy {
        SettingAppPreferences.getInstance(requireContext().dataStore)
    }
    private fun darkModeFeature() {

        settingPreferences.getThemeSetting().asLiveData().observe(viewLifecycleOwner) { isDarkModeActive ->
            binding.imageDark.tag = if (isDarkModeActive) "darkModeIcon" else "lightModeIcon"
            val iconRes = if (isDarkModeActive) R.drawable.baseline_light_mode_24 else R.drawable.baseline_dark_mode_24
            binding.imageDark.setImageResource(iconRes)
        }
        binding.imageDark.setOnClickListener {
            lifecycleScope.launch {
                val currentMode = settingPreferences.getThemeSetting().first()
                settingPreferences.saveThemeSetting(!currentMode)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        darkModeFeature()
        return binding?.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentHome.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentHome().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}