package com.viktor.kh.dev.shoplist.screens.other.backup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.BackupFragmentBinding
import com.viktor.kh.dev.shoplist.utils.showToast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BackupFragment :  Fragment(R.layout.backup_fragment) {
    private val model: BackupModel by activityViewModels()
    private lateinit var binding: BackupFragmentBinding


    private val createFileLauncher = registerForActivityResult(CreateFileContract()) { result ->
        if (result!=null){
            model.createFile(result)
            showToast(getString(R.string.backup_file_created),context)
        }else{
            showToast(getString(R.string.error),context)
        }

    }

    private val readFileLauncher = registerForActivityResult(ReadFileContract()) { result ->
        if (result!=null){
            model.readFile(result)
            showToast(getString(R.string.backup_read),context)
        }else{
            showToast(getString(R.string.backup_read_error),context)
        }
        }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?)  {
        super.onViewCreated(view, savedInstanceState)
        initActionbar()
        setHasOptionsMenu(true)
        binding = BackupFragmentBinding.bind(view)
        binding.saveFile.setOnClickListener(View.OnClickListener {
            createFileLauncher.launch("ShopList backup")

        })
        binding.downloadFile.setOnClickListener(View.OnClickListener {
           readFileLauncher.launch("")
        })
    }



    private fun initActionbar(){
        val supportActionBar: androidx.appcompat.app.ActionBar?
                = (activity as AppCompatActivity).supportActionBar
        supportActionBar?.title = getString(R.string.backup)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }





    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> activity!!.onBackPressed()

        }
        return super.onOptionsItemSelected(item)
    }





}