package com.use.rahmatkotlin.utils

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

class Constants {
    companion object{
        const val API = "http://192.168.1.2/rahmat_lappo/public/"
        const val API_ENDPOINT = API+"andro/"
        const val API_URL_IMAGES = API+"product_images/"

        fun getToken(context : Context) : String{

            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            val token = pref.getString("TOKEN", "undefined")

            return token!!
        }
        fun getID(context : Context) : Int{

            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            val id = pref.getInt("ID", 0)

            return id!!
        }

        fun setToken(context : Context, token : String, id : Int){
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit().apply {
                putString("TOKEN", token)
                putInt("ID", id)
                apply()
            }
        }
        fun clearToken(context : Context){
            val pref = context.getSharedPreferences("USER", Context.MODE_PRIVATE)
            pref.edit().clear().apply()
        }

        fun isValidEmail (email : String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        fun isValidPassword (passwrod : String) = passwrod.length > 8

    }


}