package com.viktor.kh.dev.shoplist.screens.propuctsists

import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
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
     private lateinit var rv: RecyclerView
    private lateinit var listAdapter: ProductListsAdapter

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListsBinding.bind(view)
        rv = binding.includeLists.lists
         val anim = AnimationUtils.loadLayoutAnimation(context,R.anim.bottom_layout_anim)
         initList()
         model.init()
         initActionbar()
         setHasOptionsMenu(true)
         binding.fabAddList.setOnClickListener(View.OnClickListener {
             addList()
         })
         model.dataLists.observe(viewLifecycleOwner, Observer {
             if (model.initAnim){
                 rv.layoutAnimation = anim
             }
            subscribeData(it)

       })

    }
    private fun subscribeData(data: List<DataProductList>){
      listAdapter.setData(data)
    }

   private fun initList(){
       val onListClickListener = object : ProductListsAdapter.OnListClickListener {
           override fun onListClick(position: Int) {
               val list = model.dataLists.value!![position]
               model.openList(findNavController(),list)
           }
       }

       val onDelClickListener = object : ProductListsAdapter.OnDelClickListener{
           override fun onDelClick(position: Int) {
               model.deleteList(position)
           }
       }
       val onSetClickListener = object : ProductListsAdapter.OnSetClickListener{
           override fun onSet(position: Int) {
               setList(position)
           }


       }
       listAdapter = ProductListsAdapter(onListClickListener, onSetClickListener, onDelClickListener)
       rv.apply {
           layoutManager = LinearLayoutManager(context)
           adapter = listAdapter


       }

       rv.adapter!!.notifyDataSetChanged()



   }

    private fun addList(){
        val dialog = context?.let { Dialog(it) }
        if(dialog!=null){
            dialog.setContentView(R.layout.dialog_add)
            val text = dialog.findViewById<EditText>(R.id.text_del_product)
            text.hint = getString(R.string.list_title)
            initFocusAndShowKeyboard(text, activity as AppCompatActivity)
            val buttonAdd = dialog.findViewById<Button>(R.id.btn_yes)
            val  buttonCancel = dialog.findViewById<Button>(R.id.btn_no)
            dialog.setCancelable(true)
            dialog.show()

            buttonCancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
                cancelKeyboard(text, activity as AppCompatActivity)
            })

            buttonAdd.setOnClickListener(View.OnClickListener {
                if(text.length()!=0){
                    model.addList(text.text.toString())
                    dialog.dismiss()
                }else{
                    showToast(getString(R.string.input_the_title),context)
                }
            })
        }

    }

    private fun setList(position:Int){
        var dataList: DataProductList = model.dataLists.value!![position]
        val dialog = context?.let { Dialog(it) }
        if(dialog!=null){
            dialog.setContentView(R.layout.dialog_add)
            val text = dialog.findViewById<EditText>(R.id.text_del_product)
            text.setText(dataList.name)
            initFocusAndShowKeyboard(text, activity as AppCompatActivity)
            val buttonAdd = dialog.findViewById<Button>(R.id.btn_yes)
            val  buttonCancel = dialog.findViewById<Button>(R.id.btn_no)
            dialog.setCancelable(true)
            dialog.show()

            buttonCancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
                cancelKeyboard(text, activity as AppCompatActivity)
            })

            buttonAdd.setOnClickListener(View.OnClickListener {
                if(text.text.toString().isNotEmpty()){
                    model.setList(position,text.text.toString())
                    dialog.dismiss()
                }else{

                    showToast(getString(R.string.input_the_title),activity)
                }
            })
        }



    }

    private fun initActionbar(){
        val supportActionBar: androidx.appcompat.app.ActionBar?
                = (activity as AppCompatActivity).supportActionBar
        supportActionBar?.title = getString(R.string.lists)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu_in_lists,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
          R.id.search_item -> searchList()
        }

        return super.onOptionsItemSelected(item)
    }


    fun searchList(){

    }

}