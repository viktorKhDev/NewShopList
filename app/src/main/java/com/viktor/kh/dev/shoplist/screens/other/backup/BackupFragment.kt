package com.viktor.kh.dev.shoplist.screens.other.backup

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.BackupFragmentBinding
import com.viktor.kh.dev.shoplist.utils.isNightTheme
import com.viktor.kh.dev.shoplist.utils.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BackupFragment :  Fragment(R.layout.backup_fragment) {
    private val model: BackupModel by activityViewModels()
    private lateinit var binding: BackupFragmentBinding


    private val createFileLauncher = registerForActivityResult(CreateFileContract()) { result ->
        if (result!=null){
            model.createFile(result)
        }else{
            showToast(getString(R.string.error),context)
        }

    }

    private val readFileLauncher = registerForActivityResult(ReadFileContract()) { result ->
        if (result!=null){
            model.readFile(result)
        }
        }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?)  {
        super.onViewCreated(view, savedInstanceState)
        binding = BackupFragmentBinding.bind(view)
        initActionbar()
        binding.saveFile.setOnClickListener(View.OnClickListener {
            createFileLauncher.launch("ShopList backup")

        })
        binding.downloadFile.setOnClickListener(View.OnClickListener {
           readFileLauncher.launch("")
        })
    }



    private fun initActionbar() = with(binding!!){
        toolbar.apply {
            title = getString(R.string.backup)
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