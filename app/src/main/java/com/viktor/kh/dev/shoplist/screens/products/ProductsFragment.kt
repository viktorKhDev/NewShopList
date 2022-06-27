package com.viktor.kh.dev.shoplist.screens.products


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.viktor.kh.dev.shoplist.R
import com.viktor.kh.dev.shoplist.databinding.FragmentAddBinding
import com.viktor.kh.dev.shoplist.helpers.*
import com.viktor.kh.dev.shoplist.repository.db.data.DataProduct
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductsFragment : Fragment(R.layout.fragment_add), ItemTouchAdapter {

    private val model: ProductsModel by activityViewModels()
    private lateinit var binding: FragmentAddBinding
    private lateinit var rv: RecyclerView
    private lateinit var productsAdapter: ProductsAdapter
    private  lateinit var itemTouchHelper: ItemTouchHelper
    private lateinit var itemTouchCallback: ItemTouchCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddBinding.bind(view)
        rv  = binding.listProducts
        val  listId = arguments?.getInt("listID")!!
        val anim = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_animation_fall_down)
        model.init(listId)
        binding.addProduct.setOnClickListener(View.OnClickListener {
            addProduct()
        })
        initRv()

        model.productsList.observe(viewLifecycleOwner, Observer {
            rv.layoutAnimation = anim
            subscribeData(it)

        })


    }

    private fun addProduct() = with(binding) {
        relativeAddProduct.visibility = View.VISIBLE
        addProduct.visibility = View.GONE
        Log.d("MyLog" , "addButton Hide")
        btnAcceptProduct.setOnClickListener(View.OnClickListener {
            val productName : String = textProduct.text.toString()
            if(productName.isNotEmpty()){
                val dataProduct = DataProduct(productName, currentTimeToLong().toString(), falce0)
                textProduct.setText("")
                model.addProduct(dataProduct)
            }else{
                showToast(getString(R.string.input_the_title),context)
            }

        })
        btnNoProduct.setOnClickListener(View.OnClickListener {
            textProduct.text.clear()
            relativeAddProduct.visibility = View.GONE
            addProduct.visibility = View.VISIBLE
            Log.d("MyLog" , "addButton visible")
        })

    }

    private fun setProduct(position: Int){
        //change name for product
        var dataProduct: DataProduct = model.productsList.value!![position]
        val dialog = context?.let { Dialog(it) }
        if(dialog!=null){
            dialog.setContentView(R.layout.dialog_add)
            val text = dialog.findViewById<EditText>(R.id.text_del_product)
            text.setText(dataProduct.name)
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
                    model.renameProduct(position,text.text.toString())
                    dialog.dismiss()
                }else{

                    showToast(getString(R.string.input_the_title),activity)
                }
            })
        }

    }

    private fun subscribeData(data :List<DataProduct>){
        productsAdapter.setData(data)
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

        rv.apply {
            adapter = productsAdapter
            layoutManager = LinearLayoutManager(context)
          rv.itemAnimator = null
        }

        productsAdapter.notifyDataSetChanged()
        itemTouchCallback = ItemTouchCallback(this)
        itemTouchHelper = ItemTouchHelper(itemTouchCallback)
        itemTouchHelper.attachToRecyclerView(rv)

      Log.d("MyLog", "rv init")
    }

    override fun onItemDismiss(position: Int) {
        //activate swipe from ItemTouchHelper
        model.deleteProduct(position)
    }

}