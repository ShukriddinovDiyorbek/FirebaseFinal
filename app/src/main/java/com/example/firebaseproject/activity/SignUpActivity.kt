package com.example.firebaseproject.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.firebaseproject.R
import com.example.firebaseproject.managers.AuthHandler
import com.example.firebaseproject.managers.AuthManager
import com.example.firebaseproject.utils.Extensions.toast

class SignUpActivity : BaseActivity() {
    val TAG = SignUpActivity::class.java.toString()
    lateinit var et_fullname: EditText
    lateinit var et_password: EditText
    lateinit var et_email: EditText
    lateinit var et_cpassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        initViews()
    }

    private fun initViews() {
        et_fullname = findViewById(R.id.et_fullname)
        et_email = findViewById(R.id.et_email)
        et_password = findViewById(R.id.et_password)
        et_cpassword = findViewById(R.id.et_cpassword)

        val b_signup = findViewById<Button>(R.id.b_signup)
        b_signup.setOnClickListener {
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            if(email.isNotEmpty() && password.isNotEmpty()) {
                firebaseSignUp(
                    email, password)
            } else {
                Toast.makeText(this, "Fill all gaps", Toast.LENGTH_SHORT).show()
            }

        }
        val tv_signin = findViewById<TextView>(R.id.tv_signin)
        tv_signin.setOnClickListener { callSignInActivity(this) }
    }

    fun firebaseSignUp(email: String, password: String) {
        //showLoading(this)
        AuthManager.signUp(email, password, object : AuthHandler {
            override fun onSuccess() {
                toast("Signed up successfully")
                //dismissLoading()
                callMainActivity(context)
            }

            override fun onError(exception: Exception?) {
                //dismissLoading()
                toast("Sign up failed")
            }
        })
    }
}