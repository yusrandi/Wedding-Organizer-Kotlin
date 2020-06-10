package com.use.rahmatkotlin

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProviders
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.datetime.datePicker
import com.afollestad.materialdialogs.datetime.dateTimePicker
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItems
import com.use.rahmatkotlin.adapters.ProductAdapter
import com.use.rahmatkotlin.utils.Constants
import com.use.rahmatkotlin.viewmodels.ProductViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_produk.*
import kotlinx.android.synthetic.main.toolbar_back.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object{
        const val TAG = "MainActivity"
    }
    private var debugMode = false
    private var location_id : Int = 0
    private var categorie_id : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        isLoggedIn()



        layout_lokasi.setOnClickListener { dialogLocation() }
        layout_gedung.setOnClickListener {
            categorie_id = 4;

            gotoDetail("Gedung")
        }
        layout_catering.setOnClickListener {
            categorie_id = 5;

            gotoDetail("Catering")
        }
        layout_tatarias.setOnClickListener {
            categorie_id = 6;

            gotoDetail("Tata Rias")
        }
        layout_dekorasi.setOnClickListener {
            categorie_id = 7;

            gotoDetail("Dekorasi")
        }
        layout_dokumentasi.setOnClickListener {
            categorie_id = 8;

            gotoDetail("Dokumentasi")
        }

        layout_hiburan.setOnClickListener {
            categorie_id = 9;
        gotoDetail("Hiburan")}

        goto_keranjang.setOnClickListener { startActivity(Intent(this, KeranjangActivity::class.java).apply {
            putExtra("status", "keep")
        })}



    }


    fun isLoggedIn(){
        if(Constants.getToken(this).equals("undefined")){
            startActivity(Intent(this, SignInActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }).also {
                finish()
            }
        }else{
            Log.d(TAG, "Token "+Constants.getToken(this)+" Id "+Constants.getID(this))
        }
    }

    fun dialogLocation(){
        MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            listItems(R.array.states) { _, index, text ->

                when(index){
                    0 -> location_id =737111
                    1 -> location_id =737106
                    2 -> location_id =737103
                    3 -> location_id =737102
                    4 -> location_id =737112
                    5 -> location_id =737101
                    6 -> location_id =737109
                    7 -> location_id =737113
                    8 -> location_id =737107
                    9 -> location_id =737114
                    10 -> location_id =737110
                    11 -> location_id =737104
                    12 -> location_id =737108
                    13 -> location_id =737105
                }

                toast("Selected item $text at index $index location id $location_id")
                this@MainActivity.main_et_lokasi.setText(text)


            }
            positiveButton(R.string.submit)
            negativeButton(R.string.cancel)
            debugMode(debugMode)
            lifecycleOwner(this@MainActivity)
        }

    }

    private fun gotoDetail(kat : String){
            if(location_id!=0){
                startActivity(Intent( this, ProdukActivity::class.java).apply {
                    Log.d(TAG, "loc $location_id, cat $categorie_id, Kat $kat")
                putExtra("location_id", location_id)
                putExtra("categorie_id", categorie_id)
                putExtra("kat", kat)
                })
            }else{
                toast("Silahkan Memilih Lokasi Terlebih Dahulu")
            }

    }
    fun toast(msg : String?) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    internal fun Calendar.formatDate(): String {
        return SimpleDateFormat("dd MMMM, yyyy", Locale.US).format(this.time)
    }
}
