package com.example.test

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivitytransaction : AppCompatActivity() {
    private lateinit var name:String
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        setContentView(R.layout.activity_main_activitytransaction)
        name= intent.getStringExtra("username").toString()
        val user: TextView =findViewById(R.id.user)
        user.text=name
        val thome:Button=findViewById(R.id.thome)
        val atran:TextView=findViewById(R.id.csplit)
        val htran:TextView=findViewById(R.id.agroup)
        thome.setOnClickListener {
            val intent = Intent(this,MainActivity3::class.java)

            intent.putExtra("username",name)
            startActivity(intent)
        }
atran.setOnClickListener {
    val intent = Intent(this,SubActivityaddtran::class.java)
    intent.putExtra("username",name)
    startActivity(intent)
}
htran.setOnClickListener {
    val intent = Intent(this,showtranActivity::class.java)
    intent.putExtra("username",name)
    startActivity(intent)
}

}
}