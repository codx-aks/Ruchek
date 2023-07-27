package com.example.test

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//const val BASEURL="http://192.168.1.36:8000/"
const val BASEURL="http://10.0.2.2:8000/"
class MainActivity1 : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main1)
        supportActionBar?.hide()
        val signup:Button=findViewById(R.id.signup)
        val login:Button=findViewById(R.id.login)
        val ssubmit:Button=findViewById(R.id.add)
        val lsubmit:Button=findViewById(R.id.lsubmit)
        val sname:TextView=findViewById(R.id.sname)
        val spwd:TextView=findViewById(R.id.spwd)
        val lname:TextView=findViewById(R.id.uamount)
        val lpwd:TextView=findViewById(R.id.uname)
        val next:Button=findViewById(R.id.thome)
        next.isVisible=false


        signup.setOnClickListener {
            sname.isVisible=true
            spwd.isVisible=true
            ssubmit.isVisible=true
            login.isEnabled=false
            signup.hint="a"
        }

        login.setOnClickListener {
            lname.isVisible=true
            lpwd.isVisible=true
            lsubmit.isVisible=true
            signup.isEnabled=false
            signup.hint="a"
        }
        ssubmit.setOnClickListener {
            signup()
        }

        lsubmit.setOnClickListener {
            login()
        }

        next.setOnClickListener{
            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("username",lname.text.toString())
            lname.text=""
            lpwd.text=""
            startActivity(intent)
        }




    }
    @SuppressLint("SuspiciousIndentation")
    private fun signup() {

        val retrofit= ServiceBuilder.buildService(ApiInterface::class.java)
        val sname:TextView=findViewById(R.id.sname)
        val spwd:TextView=findViewById(R.id.spwd)
        val login:Button=findViewById(R.id.login)
        val ssubmit:Button=findViewById(R.id.add)


        var snam=sname.text.toString()
        var spw=spwd.text.toString()
        val obj=input(snam,spw)
        retrofit.register(obj).enqueue(
            object: Callback<output> {

                override fun onResponse(call: Call<output>, response: Response<output>) {
                    val resp = response.body()?.output
                    var string=resp.toString()
                    Toast.makeText(this@MainActivity1,"${resp}", Toast.LENGTH_SHORT).show()
                    Log.d("s1",string )
                    if(string=="Account created successfully , kindly login"){
                        login.isEnabled=true
                        sname.isVisible=false
                        spwd.isVisible=false
                        ssubmit.isVisible=false

                    }
                }
                override fun onFailure(call: Call<output>, t: Throwable) {
                    Toast.makeText(this@MainActivity1,"registration failed", Toast.LENGTH_SHORT).show()
                    Log.d("TAG",t.localizedMessage )

                }
            }
        )
        sname.text=""
        spwd.text=""

}
    @SuppressLint("SuspiciousIndentation")
    private fun login() {

        val retrofit= ServiceBuilder.buildService(ApiInterface::class.java)
        val lname:TextView=findViewById(R.id.uamount)
        val lpwd:TextView=findViewById(R.id.uname)
        val signup:Button=findViewById(R.id.signup)

        var lnam=lname.text.toString()
        var lpw=lpwd.text.toString()
        val next:Button=findViewById(R.id.thome)
        val obj=input(lnam,lpw)
        retrofit.login(obj).enqueue(
            object: Callback<token> {

                override fun onResponse(call: Call<token>, response: Response<token>) {
                    val respo = response.body()?.msg
                    val r= response.body()?.token
                    Toast.makeText(this@MainActivity1,"${respo}", Toast.LENGTH_SHORT).show()
                    if(r=="a"){
                        lname.text=""
                        lpwd.text=""
                    }
                    else{
                        signup.isEnabled=true
                        next.isVisible=true
                    }
                }
                override fun onFailure(call: Call<token>, t: Throwable) {
                    Toast.makeText(this@MainActivity1,"login failed", Toast.LENGTH_SHORT).show()
                    Log.d("TAGlogin",t.localizedMessage )

                }
            }
        )}

}