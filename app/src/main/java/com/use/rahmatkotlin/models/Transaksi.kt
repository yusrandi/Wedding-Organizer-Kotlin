package com.use.rahmatkotlin.models

import com.google.gson.annotations.SerializedName

data class Transaksi(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("user_id") var user_id : Int = 0,
    @SerializedName("product_id") var product_id : Int = 0,
    @SerializedName("pinjam") var pinjam : String,
    @SerializedName("kembali") var kembali : String,
    @SerializedName("status") var status : String,
    @SerializedName("products") var products : Product? = null,
    @SerializedName("users") var users : User? = null


)