package com.example.tresherv2.models
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User (
    val id: String ="",
    val firstName: String ="",
    val lastName: String ="",
    val email: String ="",
    val mobile: Long =0,
    val gender: String ="",
    val profileCompleted: Int =0) : Parcelable