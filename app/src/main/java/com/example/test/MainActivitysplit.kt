package com.example.test

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivitysplit : AppCompatActivity() {
    private lateinit var name:String
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        setContentView(R.layout.activity_main_activitysplit)
        name= intent.getStringExtra("username").toString()
        val user: TextView =findViewById(R.id.user)
        user.text=name
        val shome: Button =findViewById(R.id.shome)
        val csplit:TextView=findViewById(R.id.csplit)
        val hsplit:TextView=findViewById(R.id.hsplit)
        val agroup:TextView=findViewById(R.id.agroup)
        val gsplit:TextView=findViewById(R.id.gsplit)
 shome.setOnClickListener {
     val intent = Intent(this,MainActivity3::class.java)
     intent.putExtra("username",name)
     startActivity(intent)
 }
 csplit.setOnClickListener {
     val intent = Intent(this,SubActivitycreatesplit::class.java)
     intent.putExtra("username",name)
     startActivity(intent)
 }
 hsplit.setOnClickListener {
     val intent = Intent(this,splithistoryActivity::class.java)
     intent.putExtra("username",name)
     startActivity(intent)
 }
 agroup.setOnClickListener {
     val intent = Intent(this,addgroupActivity::class.java)
     intent.putExtra("username",name)
     startActivity(intent)
 }
 gsplit.setOnClickListener {
     val intent = Intent(this,groupsplitActivity::class.java)
     intent.putExtra("username",name)
     startActivity(intent)
 }


}
}