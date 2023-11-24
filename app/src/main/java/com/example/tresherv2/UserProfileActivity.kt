package com.example.tresherv2

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.Radio
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tresherv2.firestore.FirestoreClass
import com.example.tresherv2.models.User
import com.example.tresherv2.utils.Constants
import java.io.IOException
import kotlin.math.log

class UserProfileActivity : BaseActivity(), View.OnClickListener {
    private lateinit var mUserDetails: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        val firstname = findViewById<EditText>(R.id.til_first_name)
        firstname.isEnabled = false
        firstname.setText(mUserDetails.firstName)

        val lastname = findViewById<EditText>(R.id.til_last_name)
        lastname.isEnabled = false
        lastname.setText(mUserDetails.lastName)

        val emailid = findViewById<EditText>(R.id.til_email)
        emailid.isEnabled = false
        emailid.setText(mUserDetails.email)

        val userprofile = findViewById<ImageView>(R.id.iv_user_photo)
        userprofile.setOnClickListener(this@UserProfileActivity)

    }
    private fun validateUserProfileDetails(): Boolean{
        val mobilenumber = findViewById<EditText>(R.id.til_mobile_number)
        return when{
            TextUtils.isEmpty(mobilenumber.text.toString().trim{it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else ->{
                true
            }
        }
    }

    override fun onClick(v: View?) {
        val btnsubmit = findViewById<Button>(R.id.btn_submit)
        btnsubmit.setOnClickListener(this@UserProfileActivity)
        if(v != null){
            when(v.id){
                R.id.btn_submit ->{
                    if(validateUserProfileDetails()){
                        showProgressDialog(resources.getString(R.string.please_wait))

                        updateUserProfileDetails()
                    //showErrorSnackBar("Your details are valid.", false)
                    }
                }
            }
        }
    }
    private fun updateUserProfileDetails(){
        val userHashMap = HashMap<String, Any>()
        val mobilenumber = findViewById<EditText>(R.id.til_mobile_number)
        mobilenumber.text.toString().trim { it <= ' ' }
        val btnmale = findViewById<RadioButton>(R.id.rb_male)
        val btnfemale = findViewById<RadioButton>(R.id.rb_female)
        val gender = if (btnmale.isChecked){
            Constants.MALE
        }else{
            Constants.FEMALE
        }
        if(mobilenumber.text.toString().isNotEmpty()){
            userHashMap[Constants.MOBILE] = mobilenumber.toString()
        }
        userHashMap[Constants.GENDER] = gender

        userHashMap[Constants.COMPLETE_PROFILE] = 1
       // showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().updateUserProfileData(this, userHashMap)
        //showErrorSnackBar("Your details are valid.", false)
    }
    fun userProfileUpdateSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this@UserProfileActivity, resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT
        ).show()
        startActivity(Intent(this@UserProfileActivity, HomeActivity::class.java))
        finish()
    }
}