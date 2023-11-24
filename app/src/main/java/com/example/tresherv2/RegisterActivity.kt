package com.example.tresherv2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.tresherv2.firestore.FirestoreClass
import com.example.tresherv2.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val tvlogin = findViewById<TextView>(R.id.tv_login)
        tvlogin.setOnClickListener {
            onBackPressed()
        }
        val btnregister = findViewById<Button>(R.id.btn_register)
        btnregister.setOnClickListener {
            registeruser()
        }
    }

    private fun validateRegisterDetails(): Boolean {
        val firstname = findViewById<EditText>(R.id.til_first_name)
        val lastname = findViewById<EditText>(R.id.til_last_name)
        val emailid = findViewById<EditText>(R.id.til_email)
        val passwordd = findViewById<EditText>(R.id.til_password)
        val confirmpassword = findViewById<EditText>(R.id.til_confirm_password)
        val cbtermsconditions = findViewById<CheckBox>(R.id.cb_terms_and_conditions)
        return when {
            TextUtils.isEmpty(firstname.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }

            TextUtils.isEmpty(lastname.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }

            TextUtils.isEmpty(emailid.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(passwordd.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }

            TextUtils.isEmpty(confirmpassword.text.toString().trim() { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_enter_confirm_password),
                    true
                )
                false
            }

            !cbtermsconditions.isChecked -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_agree_terms_and_conditions),
                    true
                )
                false
            }

            else -> {
                showErrorSnackBar(resources.getString(R.string.registery_successfull), false)
                true

            }
        }
    }

    private fun registeruser(){
        val firstname = findViewById<EditText>(R.id.til_first_name)
        val lastname = findViewById<EditText>(R.id.til_last_name)
        val emailid = findViewById<EditText>(R.id.til_email)
        val passwordd = findViewById<EditText>(R.id.til_password)
        val confirmpassword = findViewById<EditText>(R.id.til_confirm_password)
        val cbtermsconditions = findViewById<CheckBox>(R.id.cb_terms_and_conditions)
        if(validateRegisterDetails()){
            showProgressDialog(resources.getString(R.string.please_wait))
            val email: String = emailid.text.toString().trim{ it<= ' ' }
            val password: String = passwordd.text.toString().trim{it<= ' '}
            //create an instance and create a register a user with email id and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(
                OnCompleteListener<AuthResult>{task ->
                    //If registration is done successfully

                    if (task.isSuccessful){
                        //firebase registered user
                        val firebaseuser : FirebaseUser = task.result!!.user!!

                        val user = User(
                            firebaseuser.uid,
                            firstname.text.toString().trim { it <= ' ' },
                            lastname.text.toString().trim { it <= ' ' },
                            emailid.text.toString().trim { it <= ' ' },
                        )
                        FirestoreClass().registerUser(this@RegisterActivity, user)

                        //FirebaseAuth.getInstance().signOut();
                        //finish()
                    } else {
                        hideProgressDialog()
                        // If registration is not done succesfully
                        // showErrorSnackBar(task.exception!!.message.toString(),
                        true;
                    }
                })
        }
    }
    fun userRegistrationSuccess(){
        //Hide progress dialog
        hideProgressDialog()
        Toast.makeText(this@RegisterActivity, resources.getString(R.string.register_success),
            Toast.LENGTH_SHORT
        ).show()
    }
}