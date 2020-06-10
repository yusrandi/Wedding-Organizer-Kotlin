package com.use.rahmatkotlin.utils

import com.google.gson.annotations.SerializedName

data class WrappedResponse<T>(
    @SerializedName("message") var message : String? = null,
    @SerializedName("success") var success : String? = null,
    @SerializedName("data") var data : T? = null
)

data class WrappedListResponse<T>(
    @SerializedName("message") var message : String? = null,
    @SerializedName("success") var success : String? = null,
    @SerializedName("data") var data : List<T>? = null
)