package com.viktor.kh.dev.shoplist.screens.other.settings

import android.os.Bundle
import android.view.*

import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceFragmentCompat
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.utils.isNightTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment: PreferenceFragmentCompat() {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences,rootKey)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       val toolbar = view.findViewById<Toolbar>(R.id.pref_toolbar)
        toolbar.apply {
            title = getString(R.string.setting)
            setNavigationOnClickListener(View.OnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            })
        }
        if (isNightTheme(requireContext())){
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
            requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
        }else{
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimaryDay)
            requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimaryDay)
        }
    }


}