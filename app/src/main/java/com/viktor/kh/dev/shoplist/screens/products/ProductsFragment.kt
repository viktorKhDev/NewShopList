package com.viktor.kh.dev.shoplist.screens.products





import android.app.AlertDialog
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors.isColorLight
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.DialogAddFromRecipeBinding
import com.viktor.kh.dev.shoplist.databinding.ProductsFragmentBinding
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import com.viktor.kh.dev.shoplist.repository.db.data.DataRecipe
import com.viktor.kh.dev.shoplist.repository.db.data.ProductData
import com.viktor.kh.dev.shoplist.utils.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.products_fragment), ItemTouchAdapter {

    private val model: ProductsModel by activityViewModels()
    private lateinit var binding: ProductsFragmentBinding
    private lateinit var rv: RecyclerView
    private lateinit var productsAdapter: ProductsAdapter
    private  lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var itemTouchCallback: ItemTouchCallback
    private lateinit var fromRecipeAdapter: FromRecipeAdapter
    private var cardColor : Int? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { loadSetting(it) }
        binding = ProductsFragmentBinding.bind(view)
        cardColor = arguments?.getInt(LIST_COLOR) ?: 0
        val  listId = arguments?.getInt(LIST_ID)!!
        rv  = binding.listProducts


        cardColor?.let { color ->
            requireActivity().window.statusBarColor = color
            val insetsController = ViewCompat.getWindowInsetsController(requireActivity().window.decorView)
            insetsController?.isAppearanceLightStatusBars = isColorLight(color)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.cl) { view, insets ->
            val top = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            view.setPadding(view.paddingLeft, top, view.paddingRight, view.paddingBottom)
            insets
        }


        ViewCompat.setOnApplyWindowInsetsListener(binding.constrainAddProduct) { view, insets ->
            val imeHeight = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            val navBarHeight = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            view.setPadding(
                view.paddingLeft,
                view.paddingTop,
                view.paddingRight,
                imeHeight.coerceAtLeast(navBarHeight)
            )
            insets
        }
        binding.addProductFabInProd.setOnClickListener(View.OnClickListener {
            addProduct()
        })
        initActionbar()
        val anim = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_animation_fall_down)
        setBackgroundColor()
        model.init(listId)
        initRv()
        //initMenu()

         val callback = object :OnBackPressedCallback(true){
             override fun handleOnBackPressed() {
                 if (binding.constrainAddProduct.visibility==View.VISIBLE){
                     binding.textProduct.text.clear()
                     binding.textProduct.hideKeyboard()
                     binding.constrainAddProduct.visibility = View.GONE
                     binding.addProductFabInProd.show()
                 }else{
                    findNavController().popBackStack()
                 }
             }

         }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)

        model.productsList.observe(viewLifecycleOwner, Observer {
           if (model.initAnim){
               rv.layoutAnimation = anim
           }
            subscribeData(it)

        })



    }

    private fun addProduct() = with(binding) {
        constrainAddProduct.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.to_start_anim))
        btnNoProduct.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.fab_show_anim))
        constrainAddProduct.visibility = View.VISIBLE
        btnNoProduct.visibility = View.VISIBLE
        addProductFabInProd.hide()
        textProduct.showKeyboard()

        //for colors panel
        if(colorItems){
            colorLayout.visibility = View.VISIBLE

            val onColorClickListener = object : ColorsAdapter.OnColorClickListener{
                override fun onClick(position: Int) {
                    model.clickColor(position)
                }

            }
            val colorAdapter = ColorsAdapter(requireContext(),onColorClickListener)

            colorsPanel.apply {
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
                colorsPanel.smoothScrollToPosition(currentPosition)
            }else{
                colorsPanel.smoothScrollToPosition(0)
            }
        }else{
            colorLayout.visibility = View.GONE
        }

        //////////



        btnAcceptProduct.setOnClickListener(View.OnClickListener {
            val productName : String = textProduct.text.toString()
            if(productName.isNotEmpty()){

                textProduct.setText("")
                model.addProduct(productName)
            }else{
                showToast(getString(R.string.input_the_title),context)
            }

        })
        btnNoProduct.setOnClickListener(View.OnClickListener {
            textProduct.text.clear()
            textProduct.hideKeyboard()
            constrainAddProduct.visibility = View.GONE
            btnNoProduct.visibility = View.GONE
            addProductFabInProd.show()
            model.dataColors.removeObservers(viewLifecycleOwner)
            model.clearClick()

        })

    }

    private fun setProduct(position: Int) = with(binding){
        //change name for product
        var dataProduct: ProductData = model.productsList.value!![position]
        constrainAddProduct.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.to_start_anim))
        btnNoProduct.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.fab_show_anim))
        constrainAddProduct.visibility = View.VISIBLE
        btnNoProduct.visibility = View.VISIBLE
        addProductFabInProd.hide()
        textProduct.setText(dataProduct.name)
        textProduct.showKeyboard()


        //init colors
        val onColorClickListener = object : ColorsAdapter.OnColorClickListener{
            override fun onClick(position: Int) {
                model.clickColor(position)
            }

        }
        val colorAdapter = ColorsAdapter(requireContext(),onColorClickListener)

        colorsPanel.apply {
            layoutManager = LinearLayoutManager(context,RecyclerView.HORIZONTAL,false)
            adapter = colorAdapter
            adapter!!.notifyDataSetChanged()
        }
        model.clickColorWithColor(dataProduct.productColor)



        model.dataColors.observe(viewLifecycleOwner, Observer {
            colorAdapter.data = it
            colorAdapter.notifyDataSetChanged()

        })

        val currentPosition = colorAdapter.data.getCurrentColorPosition()
        if(currentPosition>0){
            colorsPanel.smoothScrollToPosition(currentPosition)
        }else{
            colorsPanel.smoothScrollToPosition(0)
        }


        ////

        btnAcceptProduct.setOnClickListener(View.OnClickListener {
            val productName : String = textProduct.text.toString()
            if(productName.isNotEmpty()){
                model.renameProduct(position,textProduct.text.toString())
                textProduct.text.clear()
                textProduct.hideKeyboard()
                constrainAddProduct.visibility = View.GONE
                addProductFabInProd.show()
                model.dataColors.removeObservers(viewLifecycleOwner)
            }else{
                showToast(getString(R.string.input_the_title),context)
            }



        })
        btnNoProduct.setOnClickListener(View.OnClickListener {
            textProduct.text.clear()
            textProduct.hideKeyboard()
            constrainAddProduct.visibility = View.GONE
            btnNoProduct.visibility = View.GONE
            addProductFabInProd.show()
            model.dataColors.removeObservers(viewLifecycleOwner)
        })

    }

    private fun subscribeData(data :List<ProductData>){
        productsAdapter.setData(data,model.stateChange)
        if (model.stateChange== ADD_PRODUCT){
            rv.scrollToPosition(model.forScrollToPosition)
        }
        if (model.stateChange== CHANGE_READY &&model.animPosition==0){
            rv.scrollToPosition(0)
        }

    }

    private fun initRv(){
        //init recyclerView

        val onClickListener = object : ProductsAdapter.OnProductClickListener {
            override fun onProductClick(position: Int) {
                model.changeReady(position)
            }

        }

        val onLongClickListener = object: ProductsAdapter.OnProductLongClickListener{
            override fun onProductLongClick(position: Int) {
               setProduct(position)
            }

        }
        productsAdapter = ProductsAdapter(onClickListener,onLongClickListener)
        productsAdapter.context = context
        productsAdapter.nightTheme = isNightTheme(requireContext())
        rv.apply {
            adapter = productsAdapter
            layoutManager = LinearLayoutManager(context)

            // for dev grid items
            //layoutManager = GridLayoutManager(context,3)


        }

        productsAdapter.notifyDataSetChanged()
        itemTouchCallback = ItemTouchCallback(this)
        itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(rv)


    }


    private fun initActionbar() = with(binding){
        val listName = arguments?.getString(LIST_NAME)
        if(cardColor!=0) toolbar.setBackgroundColor(cardColor!!)

        toolbar.apply {
            title = listName
            inflateMenu(R.menu.options_menu_in_list)
            setNavigationIcon(R.drawable.ic_baseline_arrow_black_24)
            setTitleTextColor(Color.BLACK)
            val itemShare = menu.findItem(R.id.share_item)
            itemShare.icon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_black_share_24)

        }
        if (isNightTheme(requireContext()) &&cardColor==0){
            toolbar.apply {
                setNavigationIcon(R.drawable.ic_baseline_arrow_white_24)
                setTitleTextColor(Color.WHITE)
                val itemShare = menu.findItem(R.id.share_item)
                itemShare.icon = ContextCompat.getDrawable(requireContext(),R.drawable.ic_day_night_share_24)
                overflowIcon = ContextCompat.getDrawable(context,R.drawable.ic_baseline_more_vert_white_24)
            }
        }
        toolbar.setOnMenuItemClickListener { item ->
            when(item.itemId){
                android.R.id.home -> {requireActivity().onBackPressedDispatcher.onBackPressed()
                    true}
                R.id.clean ->{ cleanList()
                    true}
                R.id.paste ->{model.pasteList()
                    true}
                R.id.share_item -> {activity?.let { model.shareList(it) }
                    true}
                R.id.add_recipe -> {addListFromRecipe()
                    true}
                else -> false
            }
        }
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        })




        if (cardColor != 0) {
            requireActivity().window.statusBarColor = cardColor!!
            requireActivity().window.navigationBarColor = cardColor!!
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requireActivity().window.insetsController!!.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
        }else{
            if (isNightTheme(requireContext())){
                requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
                requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
            }else{
                requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimaryDay)
                requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(),R.color.colorPrimaryDay)
            }
        }



    }


    override fun onItemDismiss(position: Int) {
        //activate swipe from ItemTouchHelper
        model.deleteProduct(position)
    }


    private fun cleanList(){
        var dialog = context?.let { Dialog(it,R.style.MyDialog) }
        if (isNightTheme(requireContext())){
            dialog = context?.let { Dialog(it,R.style.MyDialogDark) }
        }
        if(dialog!=null) {
            dialog.setContentView(R.layout.dialog_add)
            val text = dialog.findViewById<EditText>(R.id.dialog_text)
            text.setText(R.string.clear_list)
            text.isFocusable = false
            text.isClickable = false
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



    private fun setBackgroundColor() = with(binding){

        val backNoProd = btnNoProduct.background as GradientDrawable
        val backAcceptProd = btnAcceptProduct.background as GradientDrawable
        if (cardColor!=0){
            addProductFabInProd.backgroundTintList = ColorStateList.valueOf(cardColor!!)
            backNoProd.setColor(cardColor!!)
            backAcceptProd.setColor(cardColor!!)
        }else{
            var colorImage =  ColorStateList.valueOf(Color.BLACK)
            var colorBackGround = ContextCompat.getColor(requireContext(),R.color.colorPrimaryDay)

            if (isNightTheme(requireContext())){
            colorImage = ColorStateList.valueOf(Color.WHITE)
            colorBackGround = ContextCompat.getColor(requireContext(),R.color.colorPrimary)
            }
            backNoProd.setColor(colorBackGround)
            backAcceptProd.setColor(colorBackGround)
            addProductFabInProd.imageTintList = colorImage
            btnNoProduct.imageTintList = colorImage
            btnAcceptProduct.imageTintList = colorImage
        }



    }


    private fun addListFromRecipe(){

       var list = mutableListOf<DataRecipe>()
        val listValues = mutableListOf<Boolean>()
        for (i in model.getRecipesList()){
            list.add(i)
            listValues.add(false)
        }

        val view = View.inflate(context,R.layout.dialog_add_from_recipe,null)
        val dialogBinding = DialogAddFromRecipeBinding.bind(view)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()

        var mDisplayWidth:  Int? = null
        var mDisplayHeight: Int? = null

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requireActivity().windowManager.currentWindowMetrics
            mDisplayWidth = requireActivity().windowManager.currentWindowMetrics.bounds.width()
             mDisplayHeight = requireActivity().windowManager.currentWindowMetrics.bounds.height()
        } else {
           val displayMetrics = DisplayMetrics()
           requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
           mDisplayWidth = displayMetrics.widthPixels
           mDisplayHeight = displayMetrics.heightPixels
        }

        val mLayoutParams = WindowManager.LayoutParams()
        mLayoutParams.width = (mDisplayWidth * 0.8f).toInt()
        mLayoutParams.height = (mDisplayHeight * 0.5f).toInt()
        dialog.window?.setLayout(mLayoutParams.width,mLayoutParams.height)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.setWindowAnimations(R.style.DialogAnimation)

        var value = 1
        dialogBinding.countPortion.setText(value.toString())
        dialogBinding.buttonPlus.setOnClickListener(View.OnClickListener {
            if (dialogBinding.countPortion.text.isNotEmpty()){
                value = dialogBinding.countPortion.text.toString().toInt()
                value++
                if (value<1){
                    value = 1
                }
                dialogBinding.countPortion.setText(value.toString())
            }else{
                dialogBinding.countPortion.setText("1")
            }
        })
        dialogBinding.buttonMinus.setOnClickListener(View.OnClickListener {
            if (dialogBinding.countPortion.text.isNotEmpty()){
                value = dialogBinding.countPortion.text.toString().toInt()
                value--
                if (value<1){
                    value = 1
                }

                dialogBinding.countPortion.setText(value.toString())
            }else{
                dialogBinding.countPortion.setText("1")
            }

        })

        dialogBinding.btnAcceptChoice.setOnClickListener(View.OnClickListener {
            if (dialogBinding.countPortion.text.isNotEmpty()){
                model.addListFromRecipe(listValues,value)
                dialog.dismiss()
            }else{
                dialogBinding.countPortion.setText("1")
            }

        })

        val onItemClickListener = object : FromRecipeAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                listValues[position] = !listValues[position]
              fromRecipeAdapter.clickPosition(position)
            }
        }
        fromRecipeAdapter  = FromRecipeAdapter(onItemClickListener)
        fromRecipeAdapter.init(list,listValues)

        val dialogRv = dialogBinding.listRecipes

        dialogRv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = fromRecipeAdapter
        }

        fromRecipeAdapter.notifyDataSetChanged()

    }


    override fun onStop() {
        val hideAnim = AnimationUtils.loadAnimation(context,R.anim.fab_hide_anim)
        model.clearClick()
        model.currentColor = null
        hideAnim.startOffset = 200
        binding.addProductFabInProd.animation = hideAnim
        hideAnim.start()
        binding.addProductFabInProd.hide()

        super.onStop()
    }

    override fun onResume() {
        if (binding.constrainAddProduct.visibility != View.VISIBLE){
            val showAnim = AnimationUtils.loadAnimation(context,R.anim.fab_show_anim)
            showAnim.startOffset = 200
            binding.addProductFabInProd.animation = showAnim
            showAnim.start()
            binding.addProductFabInProd.show()
        }
        super.onResume()
    }

}



