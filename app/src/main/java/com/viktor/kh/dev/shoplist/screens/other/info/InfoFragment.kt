package com.viktor.kh.dev.shoplist.screens.other.info

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.viktor.kh.dev.shoplist.BuildConfig
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.SupportFragmentBinding
import com.viktor.kh.dev.shoplist.utils.isNightTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InfoFragment: Fragment(R.layout.support_fragment) {



   private var binding :SupportFragmentBinding? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SupportFragmentBinding.bind(view)
        initActionbar()
        binding!!.textVersion.text = "${requireContext().getString(R.string.app_version)}  ${BuildConfig.VERSION_NAME}"
    }




    private fun initActionbar() = with(binding!!){
        toolbar.apply {
            title = getString(R.string.support)
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