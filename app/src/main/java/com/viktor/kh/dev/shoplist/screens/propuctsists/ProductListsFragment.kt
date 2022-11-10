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
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.FragmentListsBinding
import com.viktor.kh.dev.shoplist.repository.db.data.DataProductList
import com.viktor.kh.dev.shoplist.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class ProductListsFragment: Fragment(R.layout.fragment_lists)
, FollowText.OnSearchTextChange
{

    private val model: ProductListsModel by activityViewModels()
     private lateinit var binding : FragmentListsBinding
     private lateinit var rv: RecyclerView
    private lateinit var listAdapter: ProductListsAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       context?.let { loadSetting(it) }
        binding = FragmentListsBinding.bind(view)
        rv = binding.lists
            //val anim = AnimationUtils.loadLayoutAnimation(context,R.anim.bottom_layout_anim)
         initList()
         model.init()
         initActionbar()
        binding.fabAddList.setOnClickListener(View.OnClickListener {
             addList()
         })
         model.dataLists.observe(viewLifecycleOwner, Observer {
             if (model.initAnim){
                // rv.layoutAnimation = anim
             }
            subscribeData(it)

       })

    }
    private fun subscribeData(data: List<DataProductList>){
        listAdapter.setData(data)
            if (model.isAddClicked){
                //scroll to add pos
            }
    }

   private fun initList(){
       val onListClickListener = object : ProductListsAdapter.OnListClickListener {
           override fun onListClick(position: Int) {
               val list = model.dataLists.value!![position]
               goneSearch()
               model.openList(findNavController(),list)
           }
       }

       val onDelClickListener = object : ProductListsAdapter.OnDelClickListener{
           override fun onDelClick(position: Int) {
               deleteList(position)
           }
       }
       val onSetClickListener = object : ProductListsAdapter.OnSetClickListener{
           override fun onSet(position: Int) {
               setList(position)
           }


       }
       listAdapter = ProductListsAdapter(onListClickListener, onSetClickListener, onDelClickListener)
       listAdapter.context = context
       rv.apply {
           layoutManager = LinearLayoutManager(context)
           adapter = listAdapter
       }

       rv.adapter!!.notifyDataSetChanged()



   }



    private fun addList(){

        val dialog = context?.let { Dialog(it,R.style.MyDialog) }
        if(dialog!=null){
            dialog.setContentView(R.layout.dialog_add)
            val text = dialog.findViewById<EditText>(R.id.dialog_text)
            text.hint = getString(R.string.list_title)
            val buttonAdd = dialog.findViewById<Button>(R.id.btn_yes)
            val  buttonCancel = dialog.findViewById<Button>(R.id.btn_no)
            dialog.setCancelable(true)
            dialog.show()
            text.showKeyboard()

            buttonCancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
                text.hideKeyboard()
            })

            buttonAdd.setOnClickListener(View.OnClickListener {
                if(text.length()!=0){
                    goneSearch()
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
        val dialog = context?.let { Dialog(it,R.style.MyDialog) }
        if(dialog!=null){
            dialog.setContentView(R.layout.dialog_add)
            val text = dialog.findViewById<EditText>(R.id.dialog_text)
            text.setText(dataList.name)
            val buttonAdd = dialog.findViewById<Button>(R.id.btn_yes)
            val  buttonCancel = dialog.findViewById<Button>(R.id.btn_no)
            dialog.setCancelable(true)
            dialog.show()
            text.showKeyboard()
            buttonCancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
                text.hideKeyboard()
            })

            buttonAdd.setOnClickListener(View.OnClickListener {
                goneSearch()
                if(text.text.toString().isNotEmpty()){
                    model.setList(position,text.text.toString())
                    dialog.dismiss()
                }else{

                    showToast(getString(R.string.input_the_title),activity)
                }
            })
        }



    }




    private fun deleteList(position: Int){
        val dialog = context?.let { Dialog(it,R.style.MyDialog) }
        if(dialog!=null) {
            dialog.setContentView(R.layout.dialog_add)
            val text = dialog.findViewById<EditText>(R.id.dialog_text)
            text.setText(R.string.delete_list_q)
            val buttonYes = dialog.findViewById<Button>(R.id.btn_yes)
            val buttonCancel = dialog.findViewById<Button>(R.id.btn_no)
            dialog.setCancelable(true)
            dialog.show()

            buttonYes.setOnClickListener(View.OnClickListener {
                goneSearch()
                listAdapter.deletePosition = position
                model.deleteList(position)
                dialog.dismiss()
            })

            buttonCancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
            })


        }

    }

    private fun initActionbar() = with(binding){

        if (isNightTheme(context!!)){
            activity!!.window.statusBarColor = ContextCompat.getColor(context!!,R.color.colorPrimary)
            activity!!.window.navigationBarColor = ContextCompat.getColor(context!!,R.color.colorPrimary)
        }else{
            activity!!.window.statusBarColor = ContextCompat.getColor(context!!,R.color.colorPrimaryDay)
            activity!!.window.navigationBarColor = ContextCompat.getColor(context!!,R.color.colorPrimaryDay)
        }

        listsToolbar.title = getString(R.string.lists)
        listsToolbar.inflateMenu(R.menu.options_menu_in_lists)
        listsToolbar.setOnMenuItemClickListener { item ->
            when(item.itemId){

                android.R.id.home -> {activity!!.onBackPressed()
                    true}
                R.id.search_item -> {searchList()
                    true}
                else -> false
            }
        }
        listsToolbar.setNavigationOnClickListener(View.OnClickListener {
            activity!!.onBackPressed()
        })
    }



    private fun searchList() = with(binding){
        val animation = AnimationUtils.loadAnimation(context,R.anim.to_start_anim)
        searchBar.animation = animation
        animation.start()
        searchBar.visibility  = View.VISIBLE
        autoCompleteText.addTextChangedListener(FollowText(this@ProductListsFragment))
        closeSearch.setOnClickListener(View.OnClickListener {
           goneSearch()
        })

    }


    private fun goneSearch() = with(binding){
        if (searchBar.visibility == View.VISIBLE){
            autoCompleteText.setText("")
            autoCompleteText.hideKeyboard()
            searchBar.visibility = View.GONE
            listAdapter.isSearch = true
            model.dataLists.value?.let { it1 -> subscribeData(it1) }
        }
    }


    override fun textChange(s: String) {
        val currentList = model.dataLists.value
        val list: ArrayList<DataProductList> = ArrayList()
        for (i in currentList!!) {
            if (i.name!!.lowercase(Locale.getDefault()).contains(s.lowercase(Locale.getDefault()))) {
                list.add(i)
            }
        }
        listAdapter.isSearch = true
        subscribeData(list)
    }

    override fun onStop() {
        val fabHideAnim = AnimationUtils.loadAnimation(context,R.anim.fab_hide_anim)
        fabHideAnim.startOffset = 200
        binding.fabAddList.animation = fabHideAnim
        fabHideAnim.start()
        binding.fabAddList.hide()
        goneSearch()
        super.onStop()

    }

    override fun onResume() {
        val fabShowAnim = AnimationUtils.loadAnimation(context,R.anim.fab_show_anim)
        fabShowAnim.startOffset = 200
        binding.fabAddList.animation = fabShowAnim
        fabShowAnim.start()
        binding.fabAddList.show()
        super.onResume()
    }

}