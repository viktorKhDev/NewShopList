package com.viktor.kh.dev.shoplist.screens.propuctsists


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
        initList()
         model.init()
         initActionbar()
        if (isNightTheme(requireContext())){
            binding.fabAddList.imageTintList = ColorStateList.valueOf(Color.WHITE)
        }else{
            binding.fabAddList.imageTintList = ColorStateList.valueOf(Color.BLACK)
        }
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
       listAdapter.nightTheme = isNightTheme(requireContext())
       rv.apply {
           layoutManager = LinearLayoutManager(context)
           adapter = listAdapter
       }





   }



    @SuppressLint("SuspiciousIndentation")
    private fun addList(){
        var dialog = context?.let { Dialog(it,R.style.MyDialog) }
        var listColor: Int? = null
        if (isNightTheme(requireContext())){
            dialog = context?.let { Dialog(it,R.style.MyDialogDark) }
        }
        if(dialog!=null){
            dialog.setContentView(R.layout.color_dialog_add)
            val text = dialog.findViewById<EditText>(R.id.dialog_text)
            text.hint = getString(R.string.list_title)
            val buttonAdd = dialog.findViewById<Button>(R.id.btn_yes)
            val  buttonCancel = dialog.findViewById<Button>(R.id.btn_no)
            dialog.setCancelable(true)
            dialog.setOnCancelListener {
                model.dataColors.removeObservers(viewLifecycleOwner)
            }
            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            dialog.show()
            text.showKeyboard()

            //init colors
            val onColorClickListener = object : ColorsAdapter.OnColorClickListener{
                override fun onClick(position: Int) {
                 model.clickColor(position)
                }

            }
            val colorAdapter = ColorsAdapter(requireContext(),onColorClickListener)
            val colorPanel = dialog.findViewById<RecyclerView>(R.id.colors_panel)
            colorPanel.apply {
                layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
                adapter = colorAdapter
                adapter!!.notifyDataSetChanged()
            }

            model.dataColors.observe(viewLifecycleOwner, Observer {
               colorAdapter.data = it
                   colorAdapter.notifyDataSetChanged()

            })

            val currentPosition = colorAdapter.data.getCurrentColorPosition()
            if(currentPosition>0){
                colorPanel.smoothScrollToPosition(currentPosition)
            }else{
                colorPanel.smoothScrollToPosition(0)
            }
            ////




            buttonCancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
                text.hideKeyboard()
                model.dataColors.removeObservers(viewLifecycleOwner)
            })

            buttonAdd.setOnClickListener(View.OnClickListener {
                if(text.length()!=0){
                    goneSearch()
                    model.addList(text.text.toString())
                    dialog.dismiss()
                    model.dataColors.removeObservers(viewLifecycleOwner)

                }else{
                    showToast(getString(R.string.input_the_title),context)
                }
            })
        }

    }
  
    private fun setList(position:Int){
        var dataList: DataProductList = model.dataLists.value!![position]
        var dialog = context?.let { Dialog(it,R.style.MyDialog) }
        if (isNightTheme(requireContext())){
            dialog = context?.let { Dialog(it,R.style.MyDialogDark) }
        }
        if(dialog!=null){
            dialog.setContentView(R.layout.color_dialog_add)
            val text = dialog.findViewById<EditText>(R.id.dialog_text)
            text.setText(dataList.name)
            val buttonAdd = dialog.findViewById<Button>(R.id.btn_yes)
            val  buttonCancel = dialog.findViewById<Button>(R.id.btn_no)
            dialog.setCancelable(true)
            dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            dialog.show()
            text.showKeyboard()



            //init colors
            val onColorClickListener = object : ColorsAdapter.OnColorClickListener{
                override fun onClick(position: Int) {
                    model.clickColor(position)
                }

            }
            val colorAdapter = ColorsAdapter(requireContext(),onColorClickListener)
            val colorPanel = dialog.findViewById<RecyclerView>(R.id.colors_panel)
            colorPanel.apply {
                layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
                adapter = colorAdapter
                adapter!!.notifyDataSetChanged()
            }

            model.clickColorWithColor(dataList.color)
            model.dataColors.observe(viewLifecycleOwner, Observer {
                colorAdapter.data = it
                colorAdapter.notifyDataSetChanged()

            })
            val currentPosition = colorAdapter.data.getCurrentColorPosition()
            if(currentPosition>0){
                colorPanel.smoothScrollToPosition(currentPosition)
            }else{
                colorPanel.smoothScrollToPosition(0)
            }
            ////

            buttonCancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
                text.hideKeyboard()
            })

            buttonAdd.setOnClickListener(View.OnClickListener {
                if(text.text.toString().isNotEmpty()){
                    model.setList(position,text.text.toString())
                    goneSearch()
                    dialog.dismiss()
                }else{

                    showToast(getString(R.string.input_the_title),activity)
                }
            })
        }



    }




    private fun deleteList(position: Int){
        var dialog = context?.let { Dialog(it,R.style.MyDialog) }
        if (isNightTheme(requireContext())){
            dialog = context?.let { Dialog(it,R.style.MyDialogDark) }
        }
        if(dialog!=null) {
            dialog.setContentView(R.layout.dialog_add)
            val text = dialog.findViewById<EditText>(R.id.dialog_text)
            text.setText(R.string.delete_list_q)
            text.isFocusable = false
            text.isClickable = false

            val buttonYes = dialog.findViewById<Button>(R.id.btn_yes)
            val buttonCancel = dialog.findViewById<Button>(R.id.btn_no)
            dialog.setCancelable(true)
            dialog.show()

            buttonYes.setOnClickListener(View.OnClickListener {
                listAdapter.deletePosition = position
                model.deleteList(position)
                goneSearch()
                dialog.dismiss()
            })

            buttonCancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
            })


        }

    }

    private fun initActionbar() = with(binding){

        if (isNightTheme(requireContext())){
            toBlackNavAndStatusBar(requireContext(),requireActivity())

            requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
            requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
        }else{

            requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimaryDay)
            requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimaryDay)

        }

        listsToolbar.title = getString(R.string.lists)
        listsToolbar.inflateMenu(R.menu.options_menu_in_lists)
        listsToolbar.setOnMenuItemClickListener { item ->
            when(item.itemId){

                android.R.id.home -> {requireActivity().onBackPressedDispatcher.onBackPressed()
                    true}
                R.id.search_item -> {searchList()
                    true}
                else -> false
            }
        }
        listsToolbar.setNavigationOnClickListener(View.OnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        })
    }



    private fun searchList() = with(binding){
        searchBar.visibility  = View.VISIBLE
        model.setSearchData()
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
            model.clearSearchData()

        }
    }


    override fun textChange(s: String) {
        val currentList = model.dataForSearch
        val list: ArrayList<DataProductList> = ArrayList()
        for (i in currentList) {
            if (i.name!!.lowercase(Locale.getDefault()).contains(s.lowercase(Locale.getDefault()))) {
                list.add(i)
            }
        }

        listAdapter.isSearch = true
        model.searchData(list)
        //subscribeData(list)
    }

    override fun onStop() {
        val fabHideAnim = AnimationUtils.loadAnimation(context,R.anim.fab_hide_anim)
        fabHideAnim.startOffset = 200
        binding.fabAddList.animation = fabHideAnim
        fabHideAnim.start()
        binding.fabAddList.hide()
        goneSearch()
        model.currentColor = null
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