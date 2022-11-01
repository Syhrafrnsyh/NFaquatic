package com.example.nfaquatic.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.nfaquatic.R
import com.example.nfaquatic.helper.SharedPref

class MasukActivity : AppCompatActivity() {

    lateinit var s: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masuk)
        s = SharedPref(this)
        mainButton()
    }

    private fun mainButton(){

        val btnLogin: Button = findViewById(R.id.btn_prosesLogin)
        val btnDaftar: Button = findViewById(R.id.btn_register)

        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        btnDaftar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

    }
}