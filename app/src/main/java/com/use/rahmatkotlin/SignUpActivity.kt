package com.use.rahmatkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.use.rahmatkotlin.models.User
import com.use.rahmatkotlin.utils.Constants
import com.use.rahmatkotlin.viewmodels.UserState
import com.use.rahmatkotlin.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.toolbar_back.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var userViewModel : UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        toolbar_title.setText("Sign Up")
        toolbar_back.setOnClickListener {
            finish()
        }

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.getState().observe(this, Observer {
            handleUIState(it)
        })
        doRegister()
    }



    private fun doRegister() {
        signup_click.setOnClickListener {
            val name = etName.text.toString().trim()
            val wo_name = etWoName.text.toString().trim()
            val phone = etPhone.text.toString().trim()
            val adress = etAdress.text.toString().trim()
            val email = etEmailSignUp.text.toString().trim()
            val pass = etPassSignUp.text.toString().trim()

            if(userViewModel.validate(name, wo_name, phone, adress, email, pass)){
                userViewModel.register(User(0, name, wo_name, email, phone, adress), pass)
            }
        }
    }

    private fun handleUIState(it: UserState) {
        when(it){
            is UserState.reset-> {
                setEmailError(null)
                setPassError(null)
                setNameError(null)
                setWoNameError(null)
                setPhoneError(null)
                setAdressError(null)
            }
            is UserState.Error-> {
                isLoading(false)
                toast(it.err)
            }
            is UserState.ShowToast-> toast(it.message)
            is UserState.Failed -> {
                isLoading(false)
                toast(it.message)
            }
            is UserState.Validate->{
                it.name?.let {
                    setNameError(it)
                }
                it.wo_name?.let {
                    setWoNameError(it)
                }
                it.phone?.let {
                    setPhoneError(it)
                }
                it.adress?.let {
                    setAdressError(it)
                }
                it.email?.let {
                    setEmailError(it)
                }
                it.password?.let {
                    setPassError(it)
                }
            }
            is UserState.Success->{
                Constants.setToken(this, it.token, it.id)
                startActivity(Intent(this, MainActivity::class.java ).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }).also {
                    finish()
                }
                toast("Berhasil")
            }
            is UserState.IsLoading -> isLoading(it.state)
        }
    }
    fun setNameError(err: String?){etNameLayout.error = err}
    fun setWoNameError(err: String?){etWoNameLayout.error = err}
    fun setPhoneError(err: String?){etPassLayoutSignUp.error = err}
    fun setAdressError(err: String?){etAdressLayout.error = err}
    fun setEmailError(err : String?){etEmailLayoutSignUp.error = err}
    fun setPassError(err : String?){etPassLayoutSignUp.error = err}
    fun isLoading(state : Boolean){

        if(state){
            spin_kit.visibility = View.VISIBLE
        }else{
            spin_kit.visibility = View.GONE

        }
    }
    fun toast(msg : String?) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
