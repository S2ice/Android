package com.example.talki.ui.identification

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.talki.R
import com.example.talki.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IdentificationActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_identification)
    }

    private fun checkUserState() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}