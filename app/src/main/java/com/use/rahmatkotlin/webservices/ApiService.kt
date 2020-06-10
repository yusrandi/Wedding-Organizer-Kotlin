package com.use.rahmatkotlin.webservices

import com.use.rahmatkotlin.models.Location
import com.use.rahmatkotlin.models.Product
import com.use.rahmatkotlin.models.Transaksi
import com.use.rahmatkotlin.models.User
import com.use.rahmatkotlin.utils.WrappedListResponse
import com.use.rahmatkotlin.utils.WrappedResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("users/register")
    fun register(
        @Field("name") name : String,
        @Field("wo_name") wo_name : String,
        @Field("email") email : String,
        @Field("phone") phone : String,
        @Field("adress") adress : String,
        @Field("password") password : String,
        @Field("role_id") role_id : String
    ):Call<WrappedResponse<User>>

    @FormUrlEncoded
    @POST("users/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
        ): Call<WrappedResponse<User>>

    @GET("locations")
    fun allLocation(): Call<WrappedListResponse<Location>>

    @GET("products/{id}")
    fun getOneProducts(@Path("id") id : Int) : Call<WrappedResponse<Product>>

    @GET("products")
    fun getAllProducts() : Call<WrappedListResponse<Product>>

    @GET("products/filter/{location_id}/{categorie_id}")
    fun getFilterProducts(@Path("location_id") location_id : Int, @Path("categorie_id") categorie_id : Int) : Call<WrappedListResponse<Product>>

    @FormUrlEncoded
    @POST("transaksis")
    fun addTransaksi(
        @Field("user_id") user_id: Int,
        @Field("product_id") product_id: Int,
        @Field("pinjam") pinjam: String,
        @Field("kembali") kembali: String,
        @Field("status") status: String
    ): Call<WrappedResponse<Transaksi>>

    @GET("transaksis/keranjang/{user_id}/{status}")
    fun getKeranjang(@Path("user_id") user_id : Int, @Path("status") status : String) : Call<WrappedListResponse<Transaksi>>


}