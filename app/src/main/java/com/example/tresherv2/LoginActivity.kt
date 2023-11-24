package com.example.tresherv2

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.tresherv2.models.User
import com.example.tresherv2.firestore.FirestoreClass
import com.example.tresherv2.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        val forgotpassword = findViewById<TextView>(R.id.tv_forgot_password)
        val loginbtn = findViewById<Button>(R.id.btn_login)
        // Click event assigned to forgot password text
        forgotpassword.setOnClickListener(this)
        //Click event assigned to login button
        loginbtn.setOnClickListener(this)
        //click event assigned to register text
        val tvregister = findViewById<TextView>(R.id.tv_register)
        tvregister.setOnClickListener{
            val Intent = Intent(this, RegisterActivity::class.java)
            startActivity(Intent)
        }
    }

    fun userLoggedInSuccess(user: User){
        hideProgressDialog()
        Log.d("LoginActivity", "User details: $user")
        if (user.profileCompleted==0){
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)
        } else{
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
        }
        finish()
    }

    override fun onClick(view: View??){
        if(view != null){
            when(view.id){
                R.id.tv_forgot_password ->{
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_login ->{
                    logInRegisteredUser()
                }
                R.id.tv_register ->{
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean{
        val emailid = findViewById<EditText>(R.id.til_email)
        val passwordd = findViewById<EditText>(R.id.til_password)
        return when {
            TextUtils.isEmpty(emailid.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(passwordd.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            else -> {
                true
            }
        }
    }

    private fun logInRegisteredUser(){
        if(validateLoginDetails()){
            if(validateLoginDetails()){
                showProgressDialog(resources.getString(R.string.please_wait))
                val emailid = findViewById<EditText>(R.id.til_email)
                val passwordd = findViewById<EditText>(R.id.til_password)
                val email = emailid.text.toString().trim { it <= ' ' }
                val password = passwordd.text.toString().trim { it <= ' ' }
                //Login firebase auth
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful){
                            FirestoreClass().getUserDetails(this@LoginActivity)
                        } else{
                            hideProgressDialog()
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
            }
        }
    }
}