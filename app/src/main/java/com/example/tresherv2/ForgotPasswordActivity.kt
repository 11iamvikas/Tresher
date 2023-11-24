package com.example.tresherv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val submitbtn = findViewById<Button>(R.id.btn_submit)
        val emailid = findViewById<EditText>(R.id.til_email)
        submitbtn.setOnClickListener {
            val email: String = emailid.text.toString().trim { it <= ' ' }
            if(email.isEmpty()){
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
            } else{
                showProgressDialog(resources.getString(R.string.please_wait))
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener {task ->
                        hideProgressDialog()
                        if (task.isSuccessful){
                            Toast.makeText(this@ForgotPasswordActivity,
                                resources.getString(R.string.email_sent_success),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        } else{
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    }
            }
        }
    }
}