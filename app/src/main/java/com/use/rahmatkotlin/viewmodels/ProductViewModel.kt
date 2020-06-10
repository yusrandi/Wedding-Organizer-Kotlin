package com.use.rahmatkotlin.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.use.rahmatkotlin.models.Categorie
import com.use.rahmatkotlin.models.Product
import com.use.rahmatkotlin.utils.SingleLiveEvent
import com.use.rahmatkotlin.utils.WrappedListResponse
import com.use.rahmatkotlin.utils.WrappedResponse
import com.use.rahmatkotlin.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductViewModel : ViewModel(){

    private var products = MutableLiveData<List<Product>>()
    private var product = MutableLiveData<Product>()
    private var state : SingleLiveEvent<ProductState> = SingleLiveEvent()
    private var api = ApiClient.instance()


    companion object{
        const val TAG: String = "ProductViewModel"
    }
    fun fetchAllProduct(){
        state.value = ProductState.IsLoading(true)
        api.getAllProducts().enqueue(object : Callback<WrappedListResponse<Product>>{
            override fun onFailure(call: Call<WrappedListResponse<Product>>, t: Throwable) {
                state.value = ProductState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedListResponse<Product>>, response: Response<WrappedListResponse<Product>>) {
                if(response.isSuccessful){
                    val body = response.body() as WrappedListResponse<Product>
                    if(body.success.equals("1")){
                        val r = body.data
                        products.postValue(r)
                    }
                }else{
                    state.value = ProductState.Error("Terjadi kesalahan. Gagal mendapatkan response")
                }
                state.value = ProductState.IsLoading(false)
            }

        })

    }
    fun fetchFilterProduct(location_id : Int, categorie_id : Int){
        state.value = ProductState.IsLoading(true)
        api.getFilterProducts(location_id, categorie_id).enqueue(object : Callback<WrappedListResponse<Product>>{
            override fun onFailure(call: Call<WrappedListResponse<Product>>, t: Throwable) {
                state.value = ProductState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedListResponse<Product>>, response: Response<WrappedListResponse<Product>>) {
                if(response.isSuccessful){
                    val body = response.body() as WrappedListResponse<Product>
                    if(body.success.equals("1")){
                        val r = body.data
                        products.postValue(r)
                    }
                }else{
                    state.value = ProductState.Error("Terjadi kesalahan. Gagal mendapatkan response")
                }
                state.value = ProductState.IsLoading(false)
            }

        })

    }

    fun fetchOneProduct(id : Int){
        state.value = ProductState.IsLoading(true)
        api.getOneProducts(id).enqueue(object : Callback<WrappedResponse<Product>>{
            override fun onFailure(call: Call<WrappedResponse<Product>>, t: Throwable) {
                state.value = ProductState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedResponse<Product>>, response: Response<WrappedResponse<Product>>) {
                if(response.isSuccessful){
                    val body = response.body() as WrappedResponse<Product>
                    if(body.success.equals("1")){
                        val r = body.data
                        product.postValue(r)
                    }
                }else{
                    state.value = ProductState.Error("Terjadi kesalahan. Gagal mendapatkan response")
                }
                state.value = ProductState.IsLoading(false)
            }

        })

    }

    fun getProducts() = products
    fun getOneProduct() = product
    fun getState()  = state

}
sealed class ProductState {
    data class ShowToast(var message : String) : ProductState()
    data class IsLoading(var state : Boolean = false) : ProductState()
//    data class ProductValidation(var categorie_id : String? = null, var content : String? = null) : ProductState()
    data class Error(var err : String?) : ProductState()
    data class IsSuccess(var what : Int? = null) : ProductState()
    object Reset : ProductState()
}