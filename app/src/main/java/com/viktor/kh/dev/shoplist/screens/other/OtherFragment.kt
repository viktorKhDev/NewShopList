package com.viktor.kh.dev.shoplist.screens.other

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.OtherFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherFragment : Fragment(R.layout.other_fragment){


    private val model: OtherModel by activityViewModels()
    private lateinit var binding : OtherFragmentBinding



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = OtherFragmentBinding.bind(view)
        initActionbar()
        binding.settingScreen.setOnClickListener(View.OnClickListener {
            model.openSetting(findNavController())
        })
        binding.backupScreen.setOnClickListener(View.OnClickListener {
            model.openBackup(findNavController())
        })
        binding.infoScreen.setOnClickListener(View.OnClickListener {
            model.openSupport(findNavController())
        })
        binding.privacyScreen.setOnClickListener(View.OnClickListener {
            activity?.let { it1 -> model.openPrivacy(it1) }
        })



    }




    private fun initActionbar() = with(binding){
     toolbar.title = getString(R.string.other)
    }





}