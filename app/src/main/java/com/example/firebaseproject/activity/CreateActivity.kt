package com.example.firebaseproject.activity

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.firebaseproject.R
import com.example.firebaseproject.managers.DatabaseHandler
import com.example.firebaseproject.managers.DatabaseManager
import com.example.firebaseproject.managers.StorageHandler
import com.example.firebaseproject.managers.StorageManager
import com.example.firebaseproject.model.Post
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter

class CreateActivity : BaseActivity() {
    lateinit var iv_photo: ImageView
    var postImg: Uri? = null
    var uris = ArrayList<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)
        initViews()
    }

    fun initViews() {
        var iv_close = findViewById<ImageView>(R.id.iv_close)
        var et_title = findViewById<EditText>(R.id.et_title)
        var et_body = findViewById<EditText>(R.id.et_body)
        var b_create = findViewById<Button>(R.id.b_create)
        iv_photo = findViewById<ImageView>(R.id.iv_photo)
        var iv_camera = findViewById<ImageView>(R.id.iv_camera)
        iv_camera.setOnClickListener {
            pickUserPhoto()
        }
        b_create.setOnClickListener {
            val title = et_title.text.toString().trim()
            val body = et_body.text.toString().trim()
            if(title.isNotEmpty() && body.isNotEmpty()) {
                val post = Post(title, body)
                storePost(post)
            } else {
                Toast.makeText(this, "Fill all gaps", Toast.LENGTH_SHORT).show()
            }
        }
        iv_close.setOnClickListener {
            finish()
        }

    }
    fun pickUserPhoto() {
        FishBun.with(this)
            .setImageAdapter(GlideAdapter())
            .setMaxCount(1)
            .setMinCount(1)
            .setSelectedImages(uris)
            .startAlbumWithActivityResultCallback(photoLauncher)
    }
    val photoLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                uris = it.data?.getParcelableArrayListExtra(FishBun.INTENT_PATH) ?: arrayListOf()
                postImg = uris.get(0)
                iv_photo.setImageURI(postImg)
            }
        }
    fun storePost(post: Post) {
        if (postImg != null) {
            storeStorage(post)
        } else {
            storeDatabase(post)
        }
    }
    fun storeStorage(post: Post) {
        showLoading(this)
        StorageManager.uploadPhoto(postImg!!, object : StorageHandler {
            override fun onSuccess(imgUrl: String) {
                post.img = imgUrl
                storeDatabase(post)
            }

            override fun onError(exception: Exception?) {
                storeDatabase(post)
            }
        })
    }
    fun storeDatabase(post: Post) {
        DatabaseManager.storePost(post, object : DatabaseHandler {
            override fun onSuccess(post: Post?, posts: ArrayList<Post>) {
                Log.d("@@@", "post is saved")
                dismissLoading()
                finishIntent()
            }

            override fun onError() {
                dismissLoading()
                Log.d("@@@", "post is not saved")
            }
        })
    }
    fun finishIntent() {
        val returnIntent = intent
        setResult(RESULT_OK, returnIntent)
        finish()
    }
}
