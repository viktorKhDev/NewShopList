package com.viktor.kh.dev.shoplist.screens.recipe

import android.app.ActionBar
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.RecipeFragmentBinding
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.screens.products.ProductsAdapter
import com.viktor.kh.dev.shoplist.utils.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecipeFragment : Fragment(R.layout.recipe_fragment), ItemTouchAdapter {


   private lateinit var binding : RecipeFragmentBinding
   private val model: RecipeModel by activityViewModels()
    private lateinit var rv: RecyclerView
    private lateinit var productsAdapter: RecipeProductsAdapter
    private  lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var itemTouchCallback: ItemTouchCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RecipeFragmentBinding.bind(view)
       val  listId = arguments?.getInt(listId)!!
       model.init(listId)
       initActionbar()
       initClicks()
       initRv()
       onBackCallBack()
       model.recipeText.observe(viewLifecycleOwner, Observer {
             subscribeText(it)
         })

       model.productsList.observe(viewLifecycleOwner, Observer {
           subscribeProducts(it)

       })
    }




    private fun initClicks() = with(binding){

        blackoutFrameImgTop.setOnClickListener(View.OnClickListener {
            hideRecipeProducts()

        })
        blackoutFrameImgBottom.setOnClickListener(View.OnClickListener {
            hideRecipeProducts()

        })
          recipeText.setOnClickListener(View.OnClickListener {
              recipeText.showKeyboard()

          })


        appbar.setOnClickListener(View.OnClickListener {
         recipeText.hideKeyboard()
        })

        productsFab.setOnClickListener(View.OnClickListener {
            recipeProductList.visibility = View.VISIBLE
            val alphaColor = Color.argb(50,0,0,0)
            recipeProductList.setBackgroundColor(alphaColor)
            val anim = AnimationUtils.loadAnimation(context,R.anim.scale_show_center)
            rv.startAnimation(anim)
            recipeText.hideKeyboard()
        })


        addProductFab.setOnClickListener(View.OnClickListener {
           addProduct()
        })

    }



    private fun initRv(){
        //init recyclerView
        rv  = binding.rv

        val onClickListener = object : RecipeProductsAdapter.OnProductClickListener {
            override fun onProductClick(position: Int) {
                setProduct(position)
            }

        }

        val onLongClickListener = object: RecipeProductsAdapter.OnProductLongClickListener{
            override fun onProductLongClick(position: Int) {
               // Long press action
            }

        }

        productsAdapter = RecipeProductsAdapter(onClickListener,onLongClickListener)
        rv.apply {
            adapter = productsAdapter
            layoutManager = LinearLayoutManager(context)
        }



        productsAdapter.notifyDataSetChanged()
        itemTouchCallback = ItemTouchCallback(this)
        itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(rv)


        model.getProducts()
        Log.d("MyLog", "rv init")
    }





    private fun initActionbar() = with(binding){
        val supportActionBar: androidx.appcompat.app.ActionBar
                = (activity as AppCompatActivity).supportActionBar!!
        supportActionBar.hide()
        val listName = arguments?.getString(listName)
        collapsingToolbar.title = listName
        toolbar.inflateMenu(R.menu.options_menu_in_recipe)
        toolbar.setOnMenuItemClickListener { item ->
            when(item.itemId){

               R.id.home -> activity!!.onBackPressed()

                R.id.share_item -> showToast("share in recipe",context)
            }
            false
        }

        toolbar.setNavigationOnClickListener(View.OnClickListener {
            activity!!.onBackPressed()
        })


    }



    private fun addProduct() = with(binding){
        relativeAddProduct.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.to_start_anim))
        relativeAddProduct.visibility = View.VISIBLE
        addProductFab.hide()
        textProduct.showKeyboard()
        Log.d("MyLog" , "addButton Hide")
        btnAcceptProduct.setOnClickListener(View.OnClickListener {
            val productName : String = textProduct.text.toString()
            val productAmount : String = textAmount.text.toString()

            if(productName.isNotEmpty()){
                textProduct.setText("")
                textAmount.setText("")
                model.addProduct(productName,productAmount)
                textProduct.showKeyboard()
            }else{
                showToast(getString(R.string.input_the_title),context)
            }

        })
        btnNoProduct.setOnClickListener(View.OnClickListener {
            textProduct.text.clear()
            relativeAddProduct.visibility = View.GONE
            addProductFab.show()
            textProduct.hideKeyboard()
            Log.d("MyLog" , "addButton visible")
        })
    }


    private fun setProduct(position: Int){
        //change name for product
        var dataProduct: DataProduct = model.productsList.value!![position]
        val dialog = context?.let { Dialog(it) }
        if(dialog!=null){
            dialog.setContentView(R.layout.dialog_add)
            val text = dialog.findViewById<EditText>(R.id.dialog_text)
            text.setText(dataProduct.name)
            initFocusAndShowKeyboard(text, activity as AppCompatActivity)
            val buttonYes = dialog.findViewById<Button>(R.id.btn_yes)
            val  buttonCancel = dialog.findViewById<Button>(R.id.btn_no)
            dialog.setCancelable(true)
            dialog.show()

            buttonCancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
                text.hideKeyboard()
            })

            buttonYes.setOnClickListener(View.OnClickListener {
                if(text.text.toString().isNotEmpty()){
                    model.renameProduct(position,text.text.toString())
                    dialog.dismiss()
                }else{

                    showToast(getString(R.string.input_the_title),activity)
                }
            })
        }

    }

    private fun subscribeProducts(data :List<DataProduct>){
        productsAdapter.setData(data,model.stateChange)
        if (model.stateChange== addProduct){
            rv.scrollToPosition(data.size-1)
        }

    }

    private fun subscribeText(text: String){
        binding.recipeText.setText(text)
    }


    private fun hideRecipeProducts() = with(binding){
        recipeProductList.visibility = View.GONE
        scrollText.isNestedScrollingEnabled = true
    }




    private fun onBackCallBack(){
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                   if (binding.recipeProductList.visibility == View.VISIBLE){
                       hideRecipeProducts()
                   }else{
                       findNavController().navigate(R.id.action_recipeFragment_to_recipeListsFragment)
                   }

                }

            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onPause() {
        model.saveText(binding.recipeText.text.toString())
        super.onPause()
    }

    override fun onItemDismiss(position: Int) {
       model.deleteProduct(position)
    }
}