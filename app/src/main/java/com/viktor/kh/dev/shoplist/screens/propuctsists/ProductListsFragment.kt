package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.DialogAddBinding
import com.viktor.kh.dev.shoplist.databinding.FragmentListsBinding
import com.viktor.kh.dev.shoplist.helpers.cancelKeyboard
import com.viktor.kh.dev.shoplist.helpers.initFocusAndShowKeyboard
import com.viktor.kh.dev.shoplist.helpers.showToast
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductListsFragment: Fragment(R.layout.fragment_lists)
{

    private val model: ProductListsModel by activityViewModels()
     private lateinit var binding : FragmentListsBinding
     private lateinit var list: RecyclerView
     lateinit var adapter: ProductListsAdapter

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListsBinding.bind(view)
        list = binding.includeLists.lists
         initList()
         binding.fabAddList.setOnClickListener(View.OnClickListener {
             addList()
         })
         model.dataLists.observe(viewLifecycleOwner, Observer {
            subscribeData(it)
       })

    }
    private fun subscribeData(data: List<DataProductList>){
      adapter.setData(data)
    }

   private fun initList(){
       val onListClickListener = object : ProductListsAdapter.OnListClickListener {
           override fun onListClick(position: Int) {
               //click on item
           }
       }

       val onDelClickListener = object : ProductListsAdapter.OnDelClickListener{
           override fun onDelClick(position: Int) {
               // click on del button
           }
       }
       val onSetClickListener = object : ProductListsAdapter.OnSetClickListener{
           override fun onSet(position: Int) {
               // click on det button
           }


       }
       adapter = ProductListsAdapter(onListClickListener, onSetClickListener, onDelClickListener)
       list.adapter = adapter


   }

    private fun addList(){
        val dialog = context?.let { Dialog(it) }
        if(dialog!=null){
            dialog.setContentView(R.layout.dialog_add)
            val text = dialog.findViewById<EditText>(R.id.text_del_product)
            text.hint = getString(R.string.list_title)
            //initFocusAndShowKeyboard(text, activity as AppCompatActivity)
            val buttonAdd = dialog.findViewById<Button>(R.id.btn_yes)
            val  buttonCancel = dialog.findViewById<Button>(R.id.btn_no)
            dialog.setCancelable(true)
            dialog.show()

          buttonCancel.setOnClickListener(View.OnClickListener {
              dialog.dismiss()
              //cancelKeyboard(text, activity as AppCompatActivity)
          })

            buttonAdd.setOnClickListener(View.OnClickListener {
                if(text.length()!=0){
                  model.addList(text.text.toString())
                    dialog.dismiss()
                }else{
                   showToast(R.string.input_the_title.toString(),context)
                }
            })
        }





    }

}