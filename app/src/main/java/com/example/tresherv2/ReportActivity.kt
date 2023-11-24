package com.example.tresherv2

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.MimeTypeFilter
import com.example.tresherv2.databinding.ActivityReportBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException


class ReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReportBinding
    private lateinit var database : DatabaseReference
    private var mSelectedImageFileUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val reportbutton = findViewById<Button>(R.id.btn_report)
        reportbutton.setOnClickListener {
            val intent = Intent(this, ReportActivity::class.java)
            startActivity(intent)
        }
        val submitbutton = findViewById<Button>(R.id.btn_submit)
        binding.btnSubmit.setOnClickListener {
            val wasteType = binding.tilTypeOfWaste.text.toString()
            val reportAddress = binding.tilAddress.text.toString()
            database = FirebaseDatabase.getInstance().getReference("Reports")
            val Report = Report(wasteType, reportAddress)
            database.child(wasteType).setValue(Report).addOnSuccessListener {
                binding.tilTypeOfWaste.text.clear()
                binding.tilAddress.text.clear()
            }
            if (mSelectedImageFileUri != null){

                val imageExtension = MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(contentResolver.getType(mSelectedImageFileUri!!))
                val sRef : StorageReference = FirebaseStorage.getInstance().reference.child(
                    "Image" + System.currentTimeMillis() + "." + imageExtension
                )
                val reportsubmitsuccess = findViewById<TextView>(R.id.tv_report_submit_success)
                sRef.putFile(mSelectedImageFileUri!!)
                    .addOnSuccessListener { taskSnapshot ->
                        taskSnapshot.metadata!!.reference!!.downloadUrl
                            .addOnSuccessListener {url ->
                                reportsubmitsuccess.text = "Your report was successfull :: $url"

                            }.addOnFailureListener { exception ->
                                Toast.makeText(
                                    this,
                                    exception.message,
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.e(javaClass.simpleName, exception.message, exception)
                            }
                    }
            }
            else{
                Toast.makeText(this,
                    "Please select the image to upload.",
                    Toast.LENGTH_LONG
                    ).show()
            }
        }
        val homebutton = findViewById<Button>(R.id.btn_home)
        homebutton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        val communitybutton = findViewById<Button>(R.id.btn_community)
        communitybutton.setOnClickListener {
            val intent = Intent(this, CommunityActivity::class.java)
            startActivity(intent)
        }
        val collectbutton = findViewById<Button>(R.id.btn_collect)
        collectbutton.setOnClickListener {
            val intent = Intent(this, CollectActivity::class.java)
            startActivity(intent)
        }
        val uploadimage = findViewById<Button>(R.id.btn_pick_image)
        uploadimage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED){

            }else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    121)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 121){
            val galleryIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(galleryIntent, 222)
        }else{
            Toast.makeText(this, "Oops, you just denied permission for storage. You can allow it from settings.",
                Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(data!= null){
                try {
                    mSelectedImageFileUri = data.data!!
                    val image_view = findViewById<ImageView>(R.id.imageView)
                    image_view.setImageURI(mSelectedImageFileUri)
                } catch (e: IOException){
                    e.printStackTrace()
                    Toast.makeText(
                        this@ReportActivity, "Image selection failed!",
                        Toast.LENGTH_SHORT
                    ) .show()
                }
            }
        }
    }

}