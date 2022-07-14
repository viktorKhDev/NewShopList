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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.RecipesFragmentBinding
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe
import com.viktor.kh.dev.shoplist.screens.propuctsists.ProductListsAdapter
import com.viktor.kh.dev.shoplist.utils.FollowText
import com.viktor.kh.dev.shoplist.utils.cancelKeyboard
import com.viktor.kh.dev.shoplist.utils.initFocusAndShowKeyboard
import com.viktor.kh.dev.shoplist.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class RecipeListsFragment : Fragment(R.layout.recipes_fragment)
    , FollowText.OnSearchTextChange{


    private val model :RecipeListsModel by activityViewModels()
    private lateinit var binding : RecipesFragmentBinding
    private lateinit var rv: RecyclerView
    private lateinit var recipesAdapter: RecipesAdapter
    lateinit var supportActionBar: androidx.appcompat.app.ActionBar


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RecipesFragmentBinding.bind(view)
        rv = binding.listsIncludeInRecipes.lists
        val anim = AnimationUtils.loadLayoutAnimation(context,R.anim.bottom_layout_anim)
        initList()
        model.init()
        initActionbar()
        setHasOptionsMenu(true)
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
            rv.scrollToPosition(data.size-1)

        }
    }

    private fun initList(){
        val onListClickListener = object : ProductListsAdapter.OnListClickListener {
            override fun onListClick(position: Int) {
                val list = model.dataRecipes.value!![position]
                goneSearch()
                model.openRecipe(findNavController(),list)


            }
        }

        val onDelClickListener = object : ProductListsAdapter.OnDelClickListener{
            override fun onDelClick(position: Int) {
                deleteRecipe(position)
            }
        }
        val onSetClickListener = object : ProductListsAdapter.OnSetClickListener{
            override fun onSet(position: Int) {
                setRecipe(position)
            }


        }
        recipesAdapter = RecipesAdapter(onListClickListener, onSetClickListener, onDelClickListener)
        rv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recipesAdapter


        }

        rv.adapter!!.notifyDataSetChanged()



    }

    private fun addRecipe(){

        val dialog = context?.let { Dialog(it) }
        if(dialog!=null){
            dialog.setContentView(R.layout.dialog_add)
            val text = dialog.findViewById<EditText>(R.id.dialog_text)
            text.hint = getString(R.string.input_the_title)
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
        val dialog = context?.let { Dialog(it) }
        if(dialog!=null){
            dialog.setContentView(R.layout.dialog_add)
            val text = dialog.findViewById<EditText>(R.id.dialog_text)
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
        val dialog = context?.let { Dialog(it) }
        if(dialog!=null) {
            dialog.setContentView(R.layout.dialog_add)
            val text = dialog.findViewById<EditText>(R.id.dialog_text)
            text.setText(R.string.delete_recipe_q)
            initFocusAndShowKeyboard(text, activity as AppCompatActivity)
            val buttonYes = dialog.findViewById<Button>(R.id.btn_yes)
            val buttonCancel = dialog.findViewById<Button>(R.id.btn_no)
            dialog.setCancelable(true)
            dialog.show()

            buttonYes.setOnClickListener(View.OnClickListener {
                goneSearch()
                model.deleteRecipe(position)
                dialog.dismiss()
            })

            buttonCancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
            })


        }

    }

    private fun initActionbar(){
        val supportActionBar: androidx.appcompat.app.ActionBar?
                = (activity as AppCompatActivity).supportActionBar
        supportActionBar?.title = getString(R.string.recipes)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu_in_lists,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.search_item -> searchRecipe()
        }

        return super.onOptionsItemSelected(item)
    }


    private fun searchRecipe() = with(binding.listsIncludeInRecipes){
        supportActionBar.hide()
        supportActionBar.setShowHideAnimationEnabled(false)
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
            searchBar.visibility = View.GONE
            supportActionBar.show()
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
        subscribeData(list)
    }
}