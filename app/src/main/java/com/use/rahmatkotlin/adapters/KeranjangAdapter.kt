package com.use.rahmatkotlin.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.use.rahmatkotlin.DetailProdukActivity
import com.use.rahmatkotlin.ProdukActivity
import com.use.rahmatkotlin.R
import com.use.rahmatkotlin.models.Product
import com.use.rahmatkotlin.models.Transaksi
import com.use.rahmatkotlin.utils.Constants
import kotlinx.android.synthetic.main.item_keranjang.view.*
import kotlinx.android.synthetic.main.item_list.view.*
import kotlinx.android.synthetic.main.item_list.view.item_image

class KeranjangAdapter (private var transaksis : MutableList<Transaksi>, private var context: Context) : RecyclerView.Adapter<KeranjangAdapter.ViewHolder>(){

    fun setProduct(r : List<Transaksi>){
        transaksis.clear()
        transaksis.addAll(r)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_keranjang, parent, false))
    }

    override fun getItemCount(): Int {
        return transaksis.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {holder.bind(transaksis[position], context)}

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(transaksi: Transaksi, context: Context){

            itemView.item_nama_keranjang.text = transaksi.products!!.name
            itemView.item_kategori_keranjang.text = transaksi.products!!.categories.name
            itemView.item_alamat_keranjang.text = transaksi.products!!.users.adress
            itemView.item_user_keranjang.text = transaksi.products!!.users.name
            itemView.item_harga_keranjang.text = transaksi.products!!.price.toString()

            Glide.with(itemView)  //2
                .load(Constants.API_URL_IMAGES+transaksi.products!!.image) //3
                .centerCrop() //4
                .placeholder(R.drawable.background) //5
                .error(R.drawable.background) //6
                .fallback(R.drawable.background) //7
                .into(itemView.item_image) //8

            itemView.setOnClickListener {

            }

        }
    }
}
