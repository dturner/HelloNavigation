package com.example.hellonavigation.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import kotlinx.serialization.Serializable

// Routes
@Serializable data object Home
@Serializable data class Product(val id: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a Fragment - I have no idea how to do this!

    }
}