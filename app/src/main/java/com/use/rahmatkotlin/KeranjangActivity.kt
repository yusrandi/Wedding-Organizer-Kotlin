package com.use.rahmatkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.use.rahmatkotlin.adapters.KeranjangAdapter
import com.use.rahmatkotlin.adapters.ProductAdapter
import com.use.rahmatkotlin.models.Product
import com.use.rahmatkotlin.models.Transaksi
import com.use.rahmatkotlin.utils.Constants
import com.use.rahmatkotlin.viewmodels.ProductState
import com.use.rahmatkotlin.viewmodels.TransaksiState
import com.use.rahmatkotlin.viewmodels.TransaksiViewModel
import kotlinx.android.synthetic.main.activity_keranjang.*
import kotlinx.android.synthetic.main.activity_produk.*
import java.text.NumberFormat
import java.util.*

class KeranjangActivity : AppCompatActivity() {

    companion object{
        const val TAG = "KeranjangActivity"
    }

    private lateinit var transaksiViewModel: TransaksiViewModel

    private var total : Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_keranjang)

        setupRecyler()

        transaksiViewModel = ViewModelProviders.of(this).get(TransaksiViewModel::class.java)

        transaksiViewModel.getTransaksis().observe(this, Observer {
            keranjang_rv.adapter?.let { adapter ->
                if(adapter is KeranjangAdapter){
                    adapter.setProduct(it)
                    it.forEach {
                        Log.d(TAG, "price "+it.products!!.price)
                        total = total+it.products!!.price

                    }

                    this@KeranjangActivity.keranjang_price.text = "Rp. "+NumberFormat.getNumberInstance(Locale.US).format(total)
                }
            }
        })

        transaksiViewModel.getState().observe(this, Observer {

            handleUIState(it)
        })

    }

    private fun getStatus() = intent.getStringExtra("status")
    override fun onResume() {
        super.onResume()

        Log.d(TAG, "id = ${Constants.getID(this)}, status = ${getStatus()}")
        transaksiViewModel.showKeranjang(Constants.getID(this), getStatus())
    }
    private fun setupRecyler(){
        keranjang_rv.apply {
            layoutManager = LinearLayoutManager(this@KeranjangActivity)
            adapter = KeranjangAdapter(mutableListOf(), this@KeranjangActivity)
        }
    }

    private fun handleUIState(it : TransaksiState){
        when(it){
            is TransaksiState.IsLoading -> isLoading(it.state)
            is TransaksiState.Error -> {
                toast(it.err)
                isLoading(false)
            }
        }
    }

    private fun toast(message : String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private fun isLoading(state : Boolean){
        if(state){
            loading_keranjang.visibility = View.VISIBLE
        }else{
            loading_keranjang.visibility = View.GONE
        }
    }
}
