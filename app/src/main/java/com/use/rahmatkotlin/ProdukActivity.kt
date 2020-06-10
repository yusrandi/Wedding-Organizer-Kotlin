package com.use.rahmatkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.use.rahmatkotlin.adapters.ProductAdapter
import com.use.rahmatkotlin.models.Product
import com.use.rahmatkotlin.viewmodels.ProductState
import com.use.rahmatkotlin.viewmodels.ProductViewModel
import kotlinx.android.synthetic.main.activity_detail_produk.*
import kotlinx.android.synthetic.main.activity_produk.*

class ProdukActivity : AppCompatActivity() {

    companion object{
        const val TAG = "ProdukActivity"
    }

    private lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produk)

        produk_toolbar_title.text = getKat()

        setupRecyler()

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        productViewModel.getProducts().observe(this, Observer {
            produk_rv.adapter?.let { adapter ->
                if(adapter is ProductAdapter){
                    adapter.setProduct(it)
                }
            }
        })

        productViewModel.getState().observe(this, Observer {
            handleUIState(it)
        })


    }

    private fun getLocationId() = intent.getIntExtra("location_id", 0)
    private fun getCategorieId() = intent.getIntExtra("categorie_id", 0)
    private fun getKat() = intent.getStringExtra("kat")

    private fun setupRecyler(){
        produk_rv.apply {
            layoutManager = LinearLayoutManager(this@ProdukActivity)
            adapter = ProductAdapter(mutableListOf(), this@ProdukActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        productViewModel.fetchFilterProduct(getLocationId(), getCategorieId())
    }


    private fun handleUIState(it : ProductState){
        when(it){
            is ProductState.IsLoading -> isLoading(it.state)
            is ProductState.Error -> {
                toast(it.err)
                isLoading(false)
            }
        }
    }
    private fun toast(message : String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private fun isLoading(state : Boolean){
        if(state){
            loading_produk.visibility = View.VISIBLE
        }else{
            loading_produk.visibility = View.GONE
        }
    }

}
