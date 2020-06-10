package com.use.rahmatkotlin.models

import com.google.gson.annotations.SerializedName

data class Categorie(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("name") var name : String

)