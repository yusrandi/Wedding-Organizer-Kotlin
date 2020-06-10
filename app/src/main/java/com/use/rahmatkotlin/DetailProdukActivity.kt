package com.use.rahmatkotlin

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cn.pedant.SweetAlert.SweetAlertDialog
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.bumptech.glide.Glide
import com.use.rahmatkotlin.models.Product
import com.use.rahmatkotlin.utils.Constants
import com.use.rahmatkotlin.viewmodels.*
import kotlinx.android.synthetic.main.activity_detail_produk.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class DetailProdukActivity : AppCompatActivity() {

    companion object{
        const val TAG = "DetailProdukActivity"
    }

    private lateinit var productViewModel: ProductViewModel
    private lateinit var transaksiViewModel: TransaksiViewModel

    private var debugMode = false
    private lateinit var product : Product
    private var resUserId : Int = 0
    private var resProductId : Int = 0
    private var resPinjam : String = ""
    private var resKembali : String = ""
    private var resStatus : String = "keep"

    private lateinit var pinjam : TextView
    private lateinit var kembali : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_produk)

        resUserId = Constants.getID(this)
        productViewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)
        transaksiViewModel = ViewModelProviders.of(this).get(TransaksiViewModel::class.java)

        transaksiViewModel.showKeranjang(resUserId, "keep")


        productViewModel.fetchOneProduct(getId())
        productViewModel.getOneProduct().observe(this, Observer {
            fill(it)
            product = it
        })

        productViewModel.getState().observe(this, Observer {
            handleUIState(it)
        })
        transaksiViewModel.getState().observe(this, Observer {
            handleUIStateTransaksi(it)
        })

        detail_produk_add.setOnClickListener {
            showCustomViewDialog(BottomSheet(LayoutMode.WRAP_CONTENT))
        }
        lihat_keranjang.setOnClickListener { startActivity(Intent(this, KeranjangActivity::class.java).apply {
            putExtra("status", "keep")
        })}
    }



    private fun getId() = intent.getIntExtra("id", 0)

    private fun fill(p : Product){
        resProductId = p.id!!
        detail_produk_etUser.setText(p.users.name)
        detail_produk_etalamat.setText(p.users.adress)
        detail_produk_etharga.setText(p.price.toString())
        detail_produk_etnama.setText(p.name)
        detail_produk_paket.setText(p.menu_name)
        detail_produk_etdesk.setText(Html.fromHtml(p.desc))

        Glide.with(this)  //2
            .load(Constants.API_URL_IMAGES+p.image) //3
            .centerCrop() //4
            .placeholder(R.drawable.background) //5
            .error(R.drawable.background) //6
            .fallback(R.drawable.background) //7
            .into(detail_produk_image) //8

        Log.d(TAG, "Produk categorie_id ${p.categorie_id}")

        transaksiViewModel.getTransaksis().observe(this, Observer {
            it.forEach {
                Log.d(TAG, "categorie_id ${it.products!!.categorie_id}")

                if(p.categorie_id == it.products!!.categorie_id){
                    detail_produk_add.visibility = View.GONE
                }
            }
        })

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
    private fun handleUIStateTransaksi(it: TransaksiState) {

        when(it){
            is TransaksiState.IsLoading -> isLoading(it.state)
            is TransaksiState.Error->{
                toast(it.err)
                isLoading(false)
            }
            is TransaksiState.Failed->{
                toast(it.message)
            }
            is TransaksiState.Success->{
                startActivity(Intent(this, MainActivity::class.java ).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

                }).also {
                    finish()
                }
            }

        }

    }

    private fun createTransaksi(user_id : Int, product_id : Int, pinjam : String, kembali : String, status : String){
        if(transaksiViewModel.validate(user_id, product_id, pinjam, kembali, status)){
            transaksiViewModel.createTransaksi(user_id, product_id, pinjam, kembali, status)
        }
    }
    private fun addTransaksi(){
        SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
            .setTitleText("Tambah item") //
            .setContentText("apakah anda yakin tambah ke keranjang ?")
            .setConfirmText("Ya, yakin")
            .setConfirmClickListener { sDialog ->
                toast("$resUserId, $resProductId, $resPinjam, $resKembali, $resStatus")
                createTransaksi(resUserId, resProductId, resPinjam, resKembali, resStatus)
                sDialog.dismissWithAnimation()
            }
            .setCancelButton("Batal") { ssDialog -> ssDialog.dismissWithAnimation() }
            .show()
    }

    private fun showCustomViewDialog(dialogBehavior: DialogBehavior = ModalDialog) {
        val dialog = MaterialDialog(this, dialogBehavior).show {
            title(R.string.addtransaksi)
            customView(R.layout.dialog_transaksi, scrollable = true, horizontalPadding = true)
            positiveButton(R.string.add) { dialog ->
                // Pull the password out of the custom view when the positive button is pressed

                resPinjam =pinjam.text.toString()
                resKembali =kembali.text.toString()

                addTransaksi()

            }
            negativeButton(android.R.string.cancel)
            lifecycleOwner(this@DetailProdukActivity)
            debugMode(debugMode)
        }

        // Setup custom view content
        val customView = dialog.getCustomView()
        val nama: TextView = customView.findViewById(R.id.dialog_transaksi_nama)
        val harga: TextView = customView.findViewById(R.id.dialog_transaksi_harga)
        pinjam = customView.findViewById(R.id.dialog_transaksi_pinjam)
        kembali = customView.findViewById(R.id.dialog_transaksi_kembali)

        nama.text = product.name
        harga.text = product.price.toString()

        pinjam.setOnClickListener { dialogDate(pinjam) }
        kembali.setOnClickListener { dialogDate(kembali) }




    }

    fun dialogDate(kembali: TextView) {
        MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            title(text = "Select Date and Time")
            datePicker { _, date ->
                toast("Selected date: ${date.formatDate()}")
                kembali.text = date.formatDate()
            }
            lifecycleOwner(this@DetailProdukActivity)
            debugMode(debugMode)
        }
    }

    internal fun Calendar.formatDate(): String {
        return SimpleDateFormat("dd/MMMM/yyyy", Locale.US).format(this.time)
    }

    private fun toast(message : String?) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()

    private fun isLoading(state : Boolean){
        if(state){
            layout_detail.visibility = View.GONE
            loading_detail_produk.visibility = View.VISIBLE
        }else{
            loading_detail_produk.visibility = View.GONE
            layout_detail.visibility = View.VISIBLE

        }
    }
}
