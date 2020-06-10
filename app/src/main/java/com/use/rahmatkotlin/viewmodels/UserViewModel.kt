package com.use.rahmatkotlin.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.use.rahmatkotlin.models.User
import com.use.rahmatkotlin.utils.Constants
import com.use.rahmatkotlin.utils.SingleLiveEvent
import com.use.rahmatkotlin.utils.WrappedListResponse
import com.use.rahmatkotlin.utils.WrappedResponse
import com.use.rahmatkotlin.webservices.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {

    private var state: SingleLiveEvent<UserState> = SingleLiveEvent()
    private var api = ApiClient.instance()

    fun login(email: String, password: String) {
        state.value = UserState.IsLoading(true)

        api.login(email, password).enqueue(object : Callback<WrappedResponse<User>> {
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                Log.d("UserViewModel", "onFailure " )

                state.value = UserState.Error(t.message)
            }

            override fun onResponse(call: Call<WrappedResponse<User>>, response: Response<WrappedResponse<User>>) {
                if (response.isSuccessful) {
                    Log.d("UserViewModel", "onResponse "+response.body() )

                    val body = response.body() as WrappedResponse<User>

                    if (body.success.equals("1")) {
                        state.value = UserState.Success("Use ${body.data!!.remember_token}",body.data!!.id )
                    } else {
                        state.value = UserState.Error(body.message.toString())
                    }
                } else {
                    state.value = UserState.Error("Gagal Menyambungkan ke server")

                }
                state.value = UserState.IsLoading(false)
            }

        })

    }

    fun register(user: User, password: String) {
        state.value = UserState.IsLoading(true)
        api.register(
            user.name,
            user.wo_name,
            user.email,
            user.phone,
            user.adress,
            password,
            user.role_id
        ).enqueue(object : Callback<WrappedResponse<User>> {
            override fun onFailure(call: Call<WrappedResponse<User>>, t: Throwable) {
                state.value = UserState.Error(t.message)
            }

            override fun onResponse(
                call: Call<WrappedResponse<User>>,
                response: Response<WrappedResponse<User>>
            ) {
                if (response.isSuccessful) {
                    val body = response.body() as WrappedResponse<User>
                    if (body.success.equals("1")) {
                        state.value = UserState.Success("Use ${body.data!!.remember_token}", body.data!!.id)
                    } else {
                        state.value = UserState.Error(body.message.toString())
                    }
                } else {
                    state.value = UserState.Error("Gagal Menyambungkan ke server")

                }
                state.value = UserState.IsLoading(false)
            }
        })
    }
    fun validate(name: String?, wo_name: String?, phone: String?, adress: String?, email: String, password: String) : Boolean{
        state.value = UserState.reset

        if(name != null){

            if(name.isEmpty() ){
                state.value = UserState.ShowToast("Nama Tidak Boleh Kosong")
                return false
            }
            if(name.length < 5 ){
                state.value = UserState.Validate(name = "Nama Setidaknya 5 karakter")
                return false
            }
        }
        if(wo_name != null){

            if(wo_name.isEmpty() ){
                state.value = UserState.ShowToast("WO Nama Tidak Boleh Kosong")
                return false
            }
            if(wo_name.length < 5 ){
                state.value = UserState.Validate(wo_name = "WO Nama Setidaknya 5 karakter")
                return false
            }
        }
        if(phone != null){

            if(phone.isEmpty() ){
                state.value = UserState.ShowToast("Nomor HP Tidak Boleh Kosong")
                return false
            }
            if(phone.length < 5 ){
                state.value = UserState.Validate(phone = "Nomor HP Setidaknya 5 karakter")
                return false
            }
        }
        if(adress != null){

            if(adress.isEmpty() ){
                state.value = UserState.ShowToast("Alamat Tidak Boleh Kosong")
                return false
            }
            if(adress.length < 5 ){
                state.value = UserState.Validate(adress = "Alamat Setidaknya 5 karakter")
                return false
            }
        }
        if(email.isEmpty() || password.isEmpty()){
            state.value = UserState.ShowToast("Mohon Isi Semua Form")
            return false
        }
        if(!Constants.isValidEmail(email)){
            state.value = UserState.Validate(email = "Email Tidak Valid")
            return false
        }
        if(!Constants.isValidPassword(password)){
            state.value = UserState.Validate(password = "Password Setidaknya 9 karakter")
            return false
        }

        return true
    }

    fun getState() = state

}

sealed class UserState {
    data class Error(var err: String?) : UserState()
    data class ShowToast(var message: String?) : UserState()
    data class IsLoading(var state: Boolean = false) : UserState()
    data class Success(var token: String, var id : Int) : UserState()
    data class Failed(var message: String) : UserState()
    data class Validate(
        var name: String? = null,
        var wo_name: String? = null,
        var phone: String? = null,
        var adress: String? = null,
        var email: String? = null,
        var password: String? = null
    ) : UserState()

    object reset : UserState()

}