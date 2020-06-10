package com.use.rahmatkotlin.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.use.rahmatkotlin.models.Transaksi
import com.use.rahmatkotlin.utils.Constants
import com.use.rahmatkotlin.utils.SingleLiveEvent
import com.use.rahmatkotlin.utils.WrappedListResponse
import com.use.rahmatkotlin.utils.WrappedResponse
import com.use.rahmatkotlin.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransaksiViewModel : ViewModel(){
    companion object{
        const val TAG = "TransaksiViewModel"
    }
    private var state: SingleLiveEvent<TransaksiState> = SingleLiveEvent()
    private var api = ApiClient.instance()
    private var transaksis = MutableLiveData<List<Transaksi>>()


    fun createTransaksi(user_id: Int, product_id: Int, pinjam: String, kembali: String, status: String){
        state.value = TransaksiState.IsLoading(true)
        api.addTransaksi(user_id, product_id, pinjam, kembali, status).enqueue(object : Callback<WrappedResponse<Transaksi>>{
            override fun onFailure(call: Call<WrappedResponse<Transaksi>>, t: Throwable) {
                state.value = TransaksiState.Error(t.message)

                Log.d(TAG, "onFailure : ")

            }

            override fun onResponse(call: Call<WrappedResponse<Transaksi>>, response: Response<WrappedResponse<Transaksi>>) {
                if (response.isSuccessful) {

                    Log.d(TAG, "onResponse : ${response.body()}")
                    val body = response.body() as WrappedResponse<Transaksi>

                    if (body.success.equals("1")) {
                        state.value = TransaksiState.Success(body.message.toString())
                    } else {
                        state.value = TransaksiState.Error(body.message.toString())
                    }
                } else {
                    state.value = TransaksiState.Error("Gagal Menyambungkan ke server")

                }
                state.value = TransaksiState.IsLoading(false)
            }

        })

    }
    fun showKeranjang(user_id: Int, status: String){
        state.value = TransaksiState.IsLoading(true)
        api.getKeranjang(user_id, status).enqueue(object : Callback<WrappedListResponse<Transaksi>>{
            override fun onFailure(call: Call<WrappedListResponse<Transaksi>>, t: Throwable) {
                state.value = TransaksiState.Error(t.message)

            }

            override fun onResponse(call: Call<WrappedListResponse<Transaksi>>, response: Response<WrappedListResponse<Transaksi>>) {
                if (response.isSuccessful) {

                    Log.d(TAG, "onResponse : ${response.body()}")

                    val body = response.body() as WrappedListResponse<Transaksi>

                    if (body.success.equals("1")) {
                        val r = body.data
                        transaksis.postValue(r)
                    } else {
                        state.value = TransaksiState.Error(body.message.toString())
                    }
                } else {
                    state.value = TransaksiState.Error("Gagal Menyambungkan ke server")

                }
                state.value = TransaksiState.IsLoading(false)
            }

        })

    }


    fun validate(user_id: Int, product_id: Int, pinjam: String, kembali: String, status: String) : Boolean{
        state.value = TransaksiState.reset

        if(user_id == 0 || product_id == 0 || pinjam.isEmpty() || kembali.isEmpty() || status.isEmpty()){
            state.value = TransaksiState.ShowToast("Harap Mengisi semua kolom")
                return false
        }


        return true
    }
    fun getState()  = state
    fun getTransaksis() = transaksis



}


sealed class TransaksiState{
    data class Error(var err: String?) : TransaksiState()
    data class ShowToast(var message: String?) : TransaksiState()
    data class IsLoading(var state: Boolean = false) : TransaksiState()
    data class Success(var message: String) : TransaksiState()
    data class Failed(var message: String) : TransaksiState()
    data class Validate(
        var user_id: Int? = null,
        var product_id: Int? = null,
        var pinjam: String? = null,
        var kembali: String? = null,
        var status: String? = null
    ) : TransaksiState()

    object reset : TransaksiState()
}