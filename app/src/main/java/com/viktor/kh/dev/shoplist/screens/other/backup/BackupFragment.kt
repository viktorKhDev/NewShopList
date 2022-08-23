package com.viktor.kh.dev.shoplist.screens.other.backup

import android.content.Intent
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = BackupFragmentBinding.inflate(inflater,container,false)
        binding.saveFile.setOnClickListener(View.OnClickListener {
            showToast("start create file",context)
            val createFileLauncher = registerForActivityResult(CreateFileContract()){ result ->
                model.createFile(result)
            }

            createFileLauncher.launch("")

        })
        binding.downloadFile.setOnClickListener(View.OnClickListener {
            model.readFile()
        })
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)  {
        super.onViewCreated(view, savedInstanceState)
        initActionbar()
        setHasOptionsMenu(true)
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