package com.viktor.kh.dev.shoplist.screens.recipelists

import android.app.Dialog
import android.os.Bundle
import android.util.Log
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
import com.viktor.kh.dev.shoplist.databinding.RecipesFragmentBinding
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe
import com.viktor.kh.dev.shoplist.screens.propuctsists.ProductListsAdapter
import com.viktor.kh.dev.shoplist.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RecipeListsFragment : Fragment(R.layout.recipes_fragment)
    , FollowText.OnSearchTextChange{


    private val model :RecipeListsModel by activityViewModels()
    private lateinit var binding : RecipesFragmentBinding
    private lateinit var rv: RecyclerView
    private lateinit var recipesAdapter: RecipesAdapter



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { loadSetting(it) }
        binding = RecipesFragmentBinding.bind(view)
        rv = binding.listsIncludeInRecipes.lists
        initList()
        model.init()
        initActionbar()
        binding.listsIncludeInRecipes.fabAddList.setOnClickListener(View.OnClickListener {
            addRecipe()
        })
        model.dataRecipes.observe(viewLifecycleOwner, Observer {
            subscribeData(it)
        })
    }



    private fun subscribeData(data: List<DataRecipe>){
        recipesAdapter.setData(data)
        if (model.isAddClicked){
            // scroll to last item

        }
    }

    private fun initList(){
        val onListClickListener = object : RecipesAdapter.OnListClickListener {
            override fun onListClick(position: Int) {
                val list = model.dataRecipes.value!![position]
                goneSearch()
                model.openRecipe(findNavController(),list)

            }
        }

        val onDelClickListener = object : RecipesAdapter.OnDelClickListener{
            override fun onDelClick(position: Int) {
                deleteRecipe(position)
            }
        }
        val onSetClickListener = object : RecipesAdapter.OnSetClickListener{
            override fun onSet(position: Int) {
                setRecipe(position)
            }


        }
        recipesAdapter = RecipesAdapter(onListClickListener, onSetClickListener, onDelClickListener)
        recipesAdapter.context = context
        recipesAdapter.nightTheme = isNightTheme(context!!)
        rv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recipesAdapter
        }

        rv.adapter!!.notifyDataSetChanged()

    }

    private fun addRecipe(){

        var dialog = context?.let { Dialog(it,R.style.MyDialog) }
        if (isNightTheme(context!!)){
            dialog = context?.let { Dialog(it,R.style.MyDialogDark) }
        }
        if(dialog!=null){
            dialog.setContentView(R.layout.dialog_add)
            val text = dialog.findViewById<EditText>(R.id.dialog_text)
            text.hint = getString(R.string.input_the_title)
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
                    model.addRecipe(text.text.toString())
                    dialog.dismiss()

                }else{
                    showToast(getString(R.string.input_the_title),context)
                }
            })
        }

    }
    private fun setRecipe(position:Int){
        var dataList: DataRecipe = model.dataRecipes.value!![position]
        var dialog = context?.let { Dialog(it,R.style.MyDialog) }
        if (isNightTheme(context!!)){
            dialog = context?.let { Dialog(it,R.style.MyDialogDark) }
        }
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
                    model.setRecipe(position,text.text.toString())
                    dialog.dismiss()
                }else{

                    showToast(getString(R.string.input_the_title),activity)
                }
            })
        }



    }


    private fun deleteRecipe(position: Int){
        var dialog = context?.let { Dialog(it,R.style.MyDialog) }
        if (isNightTheme(context!!)){
            dialog = context?.let { Dialog(it,R.style.MyDialogDark) }
        }
        if(dialog!=null) {
            dialog.setContentView(R.layout.dialog_add)
            val text = dialog.findViewById<EditText>(R.id.dialog_text)
            text.setText(R.string.delete_recipe_q)
            val buttonYes = dialog.findViewById<Button>(R.id.btn_yes)
            val buttonCancel = dialog.findViewById<Button>(R.id.btn_no)
            dialog.setCancelable(true)
            dialog.show()

            buttonYes.setOnClickListener(View.OnClickListener {
                goneSearch()
                recipesAdapter.deletePosition = position
                model.deleteRecipe(position)
                dialog.dismiss()
            })

            buttonCancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
            })


        }

    }

    private fun initActionbar() = with(binding.listsIncludeInRecipes) {

        if (isNightTheme(context!!)){
            activity!!.window.statusBarColor = ContextCompat.getColor(context!!,R.color.colorPrimary)
            activity!!.window.navigationBarColor = ContextCompat.getColor(context!!,R.color.colorPrimary)
        }else{
            activity!!.window.statusBarColor = ContextCompat.getColor(context!!,R.color.colorPrimaryDay)
            activity!!.window.navigationBarColor = ContextCompat.getColor(context!!,R.color.colorPrimaryDay)
        }

        listsToolbar.title = getString(R.string.recipes)
        listsToolbar.inflateMenu(R.menu.options_menu_in_lists)
        listsToolbar.setOnMenuItemClickListener { item ->
            when(item.itemId){

                R.id.search_item -> {searchRecipe()
                    true}
                else -> false
            }
        }

    }




    private fun searchRecipe() = with(binding.listsIncludeInRecipes){
        val animation = AnimationUtils.loadAnimation(context,R.anim.to_start_anim)
        searchBar.animation = animation
        animation.start()
        searchBar.visibility  = View.VISIBLE
        autoCompleteText.addTextChangedListener(FollowText(this@RecipeListsFragment))
        closeSearch.setOnClickListener(View.OnClickListener {
            goneSearch()
        })

    }


    private fun goneSearch() = with(binding.listsIncludeInRecipes){
        if (searchBar.visibility == View.VISIBLE){
            autoCompleteText.setText("")
            autoCompleteText.hideKeyboard()
            searchBar.visibility = View.GONE
            recipesAdapter.isSearch = true
            model.dataRecipes.value?.let { it1 -> subscribeData(it1) }
        }
    }


    override fun textChange(s: String) {
        val currentList = model.dataRecipes.value
        val list: ArrayList<DataRecipe> = ArrayList()
        for (i in currentList!!) {
            if (i.name!!.lowercase(Locale.getDefault()).contains(s.lowercase(Locale.getDefault()))) {
                list.add(i)
            }
        }
        recipesAdapter.isSearch = true
        subscribeData(list)
    }

    override fun onStop() {
        val fabHideAnim = AnimationUtils.loadAnimation(context,R.anim.fab_hide_anim)
        fabHideAnim.startOffset = 200
        binding.listsIncludeInRecipes.fabAddList.animation = fabHideAnim
        fabHideAnim.start()
        binding.listsIncludeInRecipes.fabAddList.hide()
        goneSearch()
        Log.d("fix", "(onStop) colorLists in recipesFragment!! =  $colorLists" )
        super.onStop()
    }

    override fun onResume() {
        val fabHideAnim = AnimationUtils.loadAnimation(context,R.anim.fab_show_anim)
        fabHideAnim.startOffset = 200
        binding.listsIncludeInRecipes.fabAddList.animation = fabHideAnim
        fabHideAnim.start()
        binding.listsIncludeInRecipes.fabAddList.show()
        Log.d("fix", "(onResume) colorLists in recipesFragment!! =  $colorLists" )
        super.onResume()
    }
}