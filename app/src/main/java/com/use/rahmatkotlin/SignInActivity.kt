package com.use.rahmatkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.use.rahmatkotlin.utils.Constants
import com.use.rahmatkotlin.viewmodels.UserState
import com.use.rahmatkotlin.viewmodels.UserViewModel
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.getState().observe(this, Observer {
            handleUIState(it)
        })
        goto_signup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        doLogin()

    }

    private fun doLogin(){
        signin_click.setOnClickListener {
            val email = etEmailSignIn.text.toString().trim()
            val pass = etPassSignIn.text.toString().trim()

            if(userViewModel.validate(null, null, null, null, email, pass)){
                userViewModel.login(email, pass)
            }
        }
    }
    private fun handleUIState(it: UserState) {
        when(it){
            is UserState.reset-> {
                setEmailError(null)
                setPassError(null)
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
    fun setEmailError(err : String?){etEmailLayoutSignIn.error = err}
    fun setPassError(err : String?){etPassLayoutSignIn.error = err}
    fun isLoading(state : Boolean){

        if(state){
            spin_kit_signin.visibility = View.VISIBLE
        }else{
            spin_kit_signin.visibility = View.GONE
        }
    }
    fun toast(msg : String?) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
