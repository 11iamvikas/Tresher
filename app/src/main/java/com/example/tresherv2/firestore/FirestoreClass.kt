package com.example.tresherv2.firestore

import android.app.Activity
import android.content.SharedPreferences
import android.util.Log
import com.example.tresherv2.LoginActivity
import com.example.tresherv2.RegisterActivity
import com.example.tresherv2.UserProfileActivity
import com.example.tresherv2.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: com.example.tresherv2.models.User){
        mFirestore.collection(com.example.tresherv2.utils.Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener{ e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserId = ""
        if (currentUserId != null){
            if (currentUser != null) {
                currentUserId = currentUser.uid
            }
        }
        return currentUserId
    }

    fun getUserDetails(activity:Activity){
        mFirestore.collection(com.example.tresherv2.utils.Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val user = document.toObject(com.example.tresherv2.models.User::class.java)!!
                //Start

                val sharedPreferences = (
                        activity.getSharedPreferences(
                            Constants.ESHOPPERS_PREFERENCES,
                            android.content.Context.MODE_PRIVATE
                        )
                        )
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                //key:value logged_in_username : vikas pant
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()
                when(activity){
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                }
                //End
            }
            .addOnFailureListener { e ->
                when(activity){
                    is LoginActivity ->{
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>){
        mFirestore.collection(Constants.USERS).document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when (activity){
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener { e->
                when (activity){
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e
                )
            }
    }
}