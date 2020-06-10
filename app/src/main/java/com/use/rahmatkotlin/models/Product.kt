package com.use.rahmatkotlin.models

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("categorie_id") var categorie_id : Int? = null,
    @SerializedName("user_id") var user_id : Int? = null,
    @SerializedName("location_id") var location_id : Int? = null,
    @SerializedName("name") var name : String,
    @SerializedName("menu_name") var menu_name : String,
    @SerializedName("image") var image : String,
    @SerializedName("price") var price : Int,
    @SerializedName("desc") var desc : String,
    @SerializedName("categories") var categories : Categorie,
    @SerializedName("users") var users : User,
    @SerializedName("locations") var locations : Location

)