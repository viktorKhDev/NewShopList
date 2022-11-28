package com.viktor.kh.dev.shoplist.screens.other.settings

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.preference.PreferenceFragmentCompat
import com.viktor.kh.dev.shoplist.R
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
                requireActivity().onBackPressed()
            })
        }
    }


}