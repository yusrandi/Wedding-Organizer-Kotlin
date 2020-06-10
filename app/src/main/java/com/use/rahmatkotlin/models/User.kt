package com.use.rahmatkotlin.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("id") var id : Int = 0,
    @SerializedName("name") var name : String = "",
    @SerializedName("wo_name") var wo_name : String = "",
    @SerializedName("email") var email : String = "",
    @SerializedName("phone") var phone : String = "",
    @SerializedName("adress") var adress : String = "",
    @SerializedName("role_id") var role_id : String = "3",
    @SerializedName("remember_token") var remember_token : String? = null
)