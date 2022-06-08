package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.DialogAddBinding

class AddDialog(_model: ProductListsModel): DialogFragment() {

     var model: ProductListsModel
    init {
        model = _model
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       var binding  = DialogAddBinding.inflate(inflater)
        binding.btnNo.setOnClickListener( View.OnClickListener {
            dismiss()
        })
        binding.btnYes.setOnClickListener(View.OnClickListener {
            model.addList(binding.textDelProduct.text.toString())
        })

        return binding.root
    }
}