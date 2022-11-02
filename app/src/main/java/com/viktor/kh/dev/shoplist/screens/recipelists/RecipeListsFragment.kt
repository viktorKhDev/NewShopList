package com.viktor.kh.dev.shoplist.screens.recipelists

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
    //lateinit var supportActionBar: androidx.appcompat.app.ActionBar


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RecipesFragmentBinding.bind(view)
        rv = binding.listsIncludeInRecipes.lists
        val anim = AnimationUtils.loadLayoutAnimation(context,R.anim.bottom_layout_anim)
        initList()
        model.init()
        initActionbar()
        initMenu()
        binding.listsIncludeInRecipes.fabAddList.setOnClickListener(View.OnClickListener {
            addRecipe()
        })
        model.dataRecipes.observe(viewLifecycleOwner, Observer {
            if (model.initAnim){
                rv.layoutAnimation = anim
            }
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
                /*   supportActionBar.setShowHideAnimationEnabled(false)
                supportActionBar.hide()*/
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
        rv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recipesAdapter


        }

        rv.adapter!!.notifyDataSetChanged()



    }

    private fun addRecipe(){

        val dialog = context?.let { Dialog(it,R.style.MyDialog) }
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
                    model.setRecipe(position,text.text.toString())
                    dialog.dismiss()
                }else{

                    showToast(getString(R.string.input_the_title),activity)
                }
            })
        }



    }


    private fun deleteRecipe(position: Int){
        val dialog = context?.let { Dialog(it,R.style.MyDialog) }
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
     /*   supportActionBar = (activity as AppCompatActivity).supportActionBar!!
        supportActionBar.apply {
            title = getString(R.string.recipes)
            setDisplayHomeAsUpEnabled(false)
            setShowHideAnimationEnabled(false)
            if (isNightTheme(context!!)){
                setBackgroundDrawable(ContextCompat.getColor(context!!,R.color.colorPrimary).toDrawable())
                activity!!.window.statusBarColor = ContextCompat.getColor(context!!,R.color.colorPrimary)
                activity!!.window.navigationBarColor = ContextCompat.getColor(context!!,R.color.colorPrimary)
            }else{
                setBackgroundDrawable(ContextCompat.getColor(context!!,R.color.colorPrimaryDay).toDrawable())
                activity!!.window.statusBarColor = ContextCompat.getColor(context!!,R.color.colorPrimaryDay)
                activity!!.window.navigationBarColor = ContextCompat.getColor(context!!,R.color.colorPrimaryDay)
            }
            show()
        }*/

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
                R.id.search_item -> {searchRecipe()
                    true}
                else -> false
            }
        }
        listsToolbar.setNavigationOnClickListener(View.OnClickListener {
            activity!!.onBackPressed()
        })

    }

    private fun initMenu(){

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.options_menu_in_lists, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.search_item -> {searchRecipe()
                        true}
                    else -> false
                }

            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }


    private fun searchRecipe() = with(binding.listsIncludeInRecipes){
       /* supportActionBar.hide()
        supportActionBar.setShowHideAnimationEnabled(false)*/
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
            //supportActionBar.show()
            listsToolbar.visibility = View.VISIBLE
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
        goneSearch()
        super.onStop()
    }
}