package com.example.test

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.Validators.or
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SubActivityaddtran : AppCompatActivity() {
    private lateinit var name:String
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        setContentView(R.layout.activity_sub_activityaddtran)
        name= intent.getStringExtra("username").toString()
//        val user: TextView =findViewById(R.id.user)
       // user.text=name
        //val tback: Button =findViewById(R.id.tback)
        /*tback.setOnClickListener {
            val intent = Intent(this,MainActivitytransaction::class.java)

            intent.putExtra("username",name)
            startActivity(intent)


        }*/
        val uname:TextView=findViewById(R.id.tusername)
        val uamount:TextView=findViewById(R.id.tamount)
        val uowe:TextView=findViewById(R.id.owesyou)
        val usettle:TextView=findViewById(R.id.settle)
        val add:Button=findViewById(R.id.addtranb)
        var a=""
        uowe.setOnClickListener{
            a="owes you"
            uowe.isVisible=false
            usettle.isVisible=true
        }
        usettle.setOnClickListener{
            a="settle"
            uowe.isVisible=true
            usettle.isVisible=false
        }
        val back:Button=findViewById(R.id.button)
        back.setOnClickListener {
            val intent = Intent(this,MainActivitytransaction::class.java)
            intent.putExtra("username",name)
            startActivity(intent)
        }
        add.setOnClickListener {
            if (a!=""){
                if(uname.text!=""){
                    if(uamount.text!=""){
                        addtran()
            }}}
            else{
                Toast.makeText(this@SubActivityaddtran,"Enter all the details", Toast.LENGTH_SHORT).show()
            }
        }





    }
    @SuppressLint("SuspiciousIndentation")

    private fun addtran() {

        val retrofit= ServiceBuilder.buildService(ApiInterface::class.java)
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASEURL)
            .build()
            .create(ApiInterface::class.java)

        retrofitBuilder.gettransactions().enqueue(
                object: Callback<transactions> {

                    override fun onResponse(call: Call<transactions>, response: Response<transactions>) {
                        val resp = response.body()
                        Log.d("TAG", "${resp}")
                        var x=0
                        var y=0
                        if (resp != null) {
                            var z= resp[(resp.size)-1]
                            x=z.tranno
                            y=z.tranid
                        }
                        else{
                            x=0
                            y=0
                        }
                        val uname:TextView=findViewById(R.id.tusername)
                        val uamount:TextView=findViewById(R.id.tamount)
                        val uowe:TextView=findViewById(R.id.owesyou)
                        val usettle:TextView=findViewById(R.id.settle)
                        var w=""
                        if(uowe.isVisible==true){
                            w="settle"
                        }
                        if(usettle.isVisible==true){
                            w="owes you"
                        }

                        val obj=transactionsItem(uamount.text.toString().toInt(),name,uname.text.toString(),w,y+1,x+1)
                        Log.d("1",uamount.text.toString() )
                        Log.d("2",name )
                        Log.d("3",uname.text.toString())
                        Log.d("4",w )
                        Log.d("5",y.toString() )
                        Log.d("6",x.toString() )

                        retrofit.addtran(obj).enqueue(
                            object: Callback<output> {
                                override fun onResponse(call: Call<output>, response: Response<output>) {
                                    val resp = response.body()?.output
                                    var string=resp.toString()
                                    Toast.makeText(this@SubActivityaddtran,"${resp}", Toast.LENGTH_SHORT).show()
                                    Log.d("s1",string )

                                    val uname:TextView=findViewById(R.id.tusername)
                                    val uamount:TextView=findViewById(R.id.tamount)
                                    val uowe:TextView=findViewById(R.id.owesyou)
                                    val usettle:TextView=findViewById(R.id.settle)
                                        uname.text=""
                                        uamount.text=""
                                        uowe.isVisible=true
                                        usettle.isVisible=true
                                }
                                override fun onFailure(call: Call<output>, t: Throwable) {
                                    Toast.makeText(this@SubActivityaddtran,"transaction addition failed", Toast.LENGTH_SHORT).show()
                                    Log.d("TAG",t.localizedMessage )
                                }
                            }
                        )

                    }
                    override fun onFailure(call: Call<transactions>, t: Throwable) {
                        Toast.makeText(this@SubActivityaddtran,t.localizedMessage,Toast.LENGTH_SHORT).show()
                        Log.d("TAG",t.localizedMessage )

                    }})





    }




}