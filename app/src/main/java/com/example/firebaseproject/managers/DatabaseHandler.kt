package com.example.firebaseproject.managers


import com.example.firebaseproject.model.Post
import java.lang.Exception

interface DatabaseHandler {
    fun onSuccess(post: Post? = null, posts: ArrayList<Post> = ArrayList())
    fun onError()
}