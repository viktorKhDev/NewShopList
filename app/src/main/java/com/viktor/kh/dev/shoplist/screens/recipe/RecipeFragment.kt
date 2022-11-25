package com.viktor.kh.dev.shoplist.screens.recipe

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = RecipeFragmentBinding.bind(view)
       val  listId = arguments?.getInt(listId)!!
       model.init(listId)
       initActionbar()
       initClicks()
       initRv()
       onBackCallBack()
       setBackgroundColor()
       val callback = object :OnBackPressedCallback(true){
           override fun handleOnBackPressed() {
               if (binding.recipeProductList.visibility==View.VISIBLE){
                  hideRecipeProducts()
               }else{
                   findNavController().popBackStack()
               }
           }

       }

       requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)
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

        btnCloseProducts.setOnClickListener(View.OnClickListener {
            hideRecipeProducts()
        })

        recipeText.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                productsFab.visibility = View.GONE
                acceptTextFab.visibility = View.VISIBLE
            }
        }

        recipeText.setOnClickListener(View.OnClickListener {
            recipeText.showKeyboard()
        })

        acceptTextFab.setOnClickListener(View.OnClickListener {
            recipeText.hideKeyboard()
            recipeText.clearFocus()
            acceptTextFab.visibility = View.GONE
            productsFab.visibility = View.VISIBLE

        })



        productsFab.setOnClickListener(View.OnClickListener {
            recipeProductList.visibility = View.VISIBLE
            val alphaColor = Color.argb(50,0,0,0)
            recipeProductList.setBackgroundColor(alphaColor)
            val anim = AnimationUtils.loadAnimation(context,R.anim.scale_show_center)
            rv.startAnimation(anim)
            recipeText.hideKeyboard()
            val buttonAnim = AnimationUtils.loadAnimation(context,R.anim.alpha_anim)
            btnCloseProducts.startAnimation(buttonAnim)
            btnCloseProducts.visibility  = View.VISIBLE
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
                // Press action
            }

        }

        val onLongClickListener = object: RecipeProductsAdapter.OnProductLongClickListener{
            override fun onProductLongClick(position: Int) {
                setProduct(position)
            }

        }

        productsAdapter = RecipeProductsAdapter(onClickListener,onLongClickListener)
        productsAdapter.context = context
        rv.apply {
            adapter = productsAdapter
            layoutManager = LinearLayoutManager(context)
        }



        productsAdapter.notifyDataSetChanged()
        itemTouchCallback = ItemTouchCallback(this)
        itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(rv)


        model.getProducts()

    }

    private fun setBackgroundColor() = with(binding){
        //set color from recipes lists item
        if (colorLists){
            if (currentCardColor!=0){
                addProductFab.backgroundTintList = ColorStateList.valueOf(currentCardColor)
                productsFab.backgroundTintList = ColorStateList.valueOf(currentCardColor)
                acceptTextFab.backgroundTintList = ColorStateList.valueOf(currentCardColor)

                val backgroundShape = btnCloseProducts.background as GradientDrawable
                backgroundShape.setColor(currentCardColor)

                val backNoProd = btnNoProduct.background as GradientDrawable
                backNoProd.setColor(currentCardColor)

                val backAcceptProd = btnAcceptProduct.background as GradientDrawable
                backAcceptProd.setColor(currentCardColor)

            }
        }else{
            
        }

    }




    private fun initActionbar() = with(binding){
     /*   val supportActionBar = (activity as AppCompatActivity).supportActionBar!!
        supportActionBar.hide()*/
        val listName = arguments?.getString(LIST_NAME)
        collapsingToolbar.title = listName
        recipeToolbar.inflateMenu(R.menu.options_menu_in_recipe)
        
        Log.d("fix","currentCardColor = ${currentCardColor.toString()}" )

            if (colorLists){
                if (currentCardColor!=0) {
                    activity!!.window.statusBarColor = currentCardColor
                    activity!!.window.navigationBarColor = currentCardColor
                    collapsingToolbar.setBackgroundColor(currentCardColor)
                    recipeToolbar.setBackgroundColor(currentCardColor)
                    recipeCoordinatorLayout.setBackgroundColor(currentCardColor)
                }
            }

            collapsingToolbar.setCollapsedTitleTextColor(Color.BLACK)
            collapsingToolbar.setExpandedTitleColor(Color.BLACK)


        recipeToolbar.setOnMenuItemClickListener { item ->
            when(item.itemId){

               R.id.home -> activity!!.onBackPressed()

                R.id.share_item -> listName?.let {
                    model.shareRecipe(activity as AppCompatActivity,
                        it
                    )
                }

                R.id.paste -> model.pasteProducts()

                R.id.clear_products -> clearProducts()

            }
            false
        }

        recipeToolbar.setNavigationOnClickListener(View.OnClickListener {
            activity!!.onBackPressed()
        })


    }



    private fun addProduct() = with(binding){
        relativeAddProduct.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.to_start_anim))
        relativeAddProduct.visibility = View.VISIBLE
        addProductFab.hide()
        textProduct.showKeyboard()



        Log.d("MyLog" , "addButton Hide")
        textProduct.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_NEXT){

                textAmount.showKeyboard()
                handled = true
            }
            handled
        })
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
            textAmount.text.clear()
            relativeAddProduct.visibility = View.GONE
            addProductFab.show()
            textProduct.hideKeyboard()
            Log.d("MyLog" , "addButton visible")
        })
    }


    private fun setProduct(position: Int){
        //change name for product
        var dataProduct: DataProduct = model.productsList.value!![position]
        var dialog = context?.let { Dialog(it,R.style.MyDialog) }
        if (isNightTheme(context!!)){
            dialog = context?.let { Dialog(it,R.style.MyDialogDark) }
        }
        if(dialog!=null){
            dialog.setContentView(R.layout.dialog_set_recipe)
            val text = dialog.findViewById<EditText>(R.id.dialog_text)
            val amount = dialog.findViewById<EditText>(R.id.dialog_amount)
            text.setText(dataProduct.name)
            if (dataProduct.amount!=null){
                amount.setText(dataProduct.amount)
            }
            val buttonYes = dialog.findViewById<Button>(R.id.btn_yes)
            val  buttonCancel = dialog.findViewById<Button>(R.id.btn_no)
            dialog.setCancelable(true)
            dialog.show()
            text.showKeyboard()

            buttonCancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
                text.hideKeyboard()
            })

            buttonYes.setOnClickListener(View.OnClickListener {
                if(text.text.toString().isNotEmpty()){
                    model.renameProduct(position,text.text.toString(),amount.text.toString())
                    dialog.dismiss()
                }else{
                    showToast(getString(R.string.input_the_title),activity)
                }
            })
        }

    }

    private fun subscribeProducts(data :List<DataProduct>){
        productsAdapter.setData(data,model.stateChange)
        if (model.stateChange== ADD_PRODUCT){
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


    private fun clearProducts(){
        var dialog = context?.let { Dialog(it,R.style.MyDialog) }
        if (isNightTheme(context!!)){
            dialog = context?.let { Dialog(it,R.style.MyDialogDark) }
        }
        if(dialog!=null) {
            dialog.setContentView(R.layout.dialog_add)
            val text = dialog.findViewById<EditText>(R.id.dialog_text)
            text.setText(R.string.clean_products_list)
            val buttonYes = dialog.findViewById<Button>(R.id.btn_yes)
            val buttonCancel = dialog.findViewById<Button>(R.id.btn_no)
            dialog.setCancelable(true)
            dialog.show()

            buttonYes.setOnClickListener(View.OnClickListener {
                model.cleanList()
                dialog.dismiss()
            })

            buttonCancel.setOnClickListener(View.OnClickListener {
                dialog.dismiss()
            })


        }
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