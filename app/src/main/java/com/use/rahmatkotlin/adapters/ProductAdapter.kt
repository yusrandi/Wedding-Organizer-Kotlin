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
import com.use.rahmatkotlin.utils.Constants
import kotlinx.android.synthetic.main.item_list.view.*

class ProductAdapter (private var products : MutableList<Product>, private var context: Context) : RecyclerView.Adapter<ProductAdapter.ViewHolder>(){

    fun setProduct(r : List<Product>){
        products.clear()
        products.addAll(r)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {holder.bind(products[position], context)}

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(product : Product, context: Context){

            itemView.item_nama.text = product.name
            itemView.item_alamat.text = product.users.adress
            itemView.item_user.text = product.users.name
            itemView.item_harga.text = product.price.toString()

            Glide.with(itemView)  //2
                .load(Constants.API_URL_IMAGES+product.image) //3
                .centerCrop() //4
                .placeholder(R.drawable.background) //5
                .error(R.drawable.background) //6
                .fallback(R.drawable.background) //7
                .into(itemView.item_image) //8

            itemView.setOnClickListener {
                context.startActivity(Intent( context, DetailProdukActivity::class.java).apply {
                    putExtra("id", product.id)
                })
            }

        }
    }
}
