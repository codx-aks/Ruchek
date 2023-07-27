package com.example.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
        setContentView(R.layout.activity_main2)
        supportActionBar?.hide()
        val intro:TextView=findViewById(R.id.intro)
        val name=intent.getStringExtra("username")
        intro.text=name

        Handler().postDelayed({

            val intent = Intent(this, MainActivity3::class.java)
            intent.putExtra("username",name)
            startActivity(intent)

            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        },3500)
    }
}