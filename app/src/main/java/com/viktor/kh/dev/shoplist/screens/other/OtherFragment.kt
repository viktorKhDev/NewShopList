package com.viktor.kh.dev.shoplist.screens.other

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.OtherFragmentBinding
import com.viktor.kh.dev.shoplist.utils.isNightTheme
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
        if (isNightTheme(requireContext())){
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
            requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
        }else{
            requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimaryDay)
            requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimaryDay)
        }
    }





}