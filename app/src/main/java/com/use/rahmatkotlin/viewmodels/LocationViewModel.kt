package com.use.rahmatkotlin.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.use.rahmatkotlin.models.Location
import com.use.rahmatkotlin.utils.SingleLiveEvent
import com.use.rahmatkotlin.utils.WrappedListResponse
import com.use.rahmatkotlin.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationViewModel : ViewModel(){
    private var recipes = MutableLiveData<List<Location>>()
    private var recipe = MutableLiveData<Location>()
    private var state: SingleLiveEvent<LocationState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    fun fetchAllLocation(){
        state.value = LocationState.isLoading(true)

        api.allLocation().enqueue(object : Callback<WrappedListResponse<Location>>{
            override fun onFailure(call: Call<WrappedListResponse<Location>>, t: Throwable) {
                state.value = LocationState.isError(t.message)

            }

            override fun onResponse(call: Call<WrappedListResponse<Location>>, response: Response<WrappedListResponse<Location>>) {
                if (response.isSuccessful) {
                    val body = response.body() as WrappedListResponse<Location>
                    if (body.success.equals("1")) {

                        val r = body.data
                        recipes.postValue(r)

                    }
                } else {
                    state.value = LocationState.isError("Terjadi kesalahan, Gagal Mendapatkan response")

                }
                state.value = LocationState.isLoading(false)
            }

        })
    }
}
sealed class LocationState{
    data class isLoading(var state: Boolean = false) : LocationState()
    data class isError(var err: String?) : LocationState()

}