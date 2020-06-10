package com.use.rahmatkotlin.models

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("nama") var nama : String = ""

)