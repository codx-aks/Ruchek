package com.example.test

import com.example.test.ApiInterface
import com.example.test.BASEURL
import com.example.test.MainActivitysplit
import com.example.test.R
import com.example.test.ServiceBuilder
import com.example.test.output



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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class addgroupActivity : AppCompatActivity() {
    private lateinit var name:String
    private lateinit var gnames:MutableList<String>

    private var No:Int=0

    private var i=0

    @SuppressLint("MissingInflatedId")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        setContentView(R.layout.activity_addgroup)
        name= intent.getStringExtra("username").toString()
        val user: TextView =findViewById(R.id.user)
        user.text=name
        val sback: Button =findViewById(R.id.sback)
        sback.setOnClickListener {
            val intent = Intent(this, MainActivitysplit::class.java)

            intent.putExtra("username",name)
            startActivity(intent)
        }
        val gtitleget: TextView =findViewById(R.id.gtitleget)

        val gpeopleget: TextView =findViewById(R.id.gpeopleget)
        val group: Button =findViewById(R.id.groupb)
        val gname: TextView =findViewById(R.id.gname)
        val groupnext: Button =findViewById(R.id.addp)
        val groupheading: TextView =findViewById(R.id.groupheading)
        gnames= mutableListOf()
        group.setOnClickListener {
            if(gtitleget.text!=""){
                    if(gpeopleget.text!=""){
                        groupheading.text=gtitleget.text.toString()

                        No=gpeopleget.text.toString().toInt()
                        gname.isVisible=true
                        groupnext.isVisible=true
                        groupheading.isVisible=true
                        gtitleget.isVisible=false

                        gpeopleget.isVisible=false
                        group.isVisible=false
                        gtitleget.text=""

                        gpeopleget.text=""


                } } }

        groupnext.setOnClickListener {
            if(i!=No){
                if(gname.text!=""){
                    gnames.add(gname.text.toString())

                    addgroup()
                }
            if(i==No-2){
                    groupnext.text="Create Group"
            }
                if(i==No-1){

                    gname.isVisible=false
                    groupnext.isVisible=false
                    groupheading.isVisible=false

                    gtitleget.isVisible=true

                    gpeopleget.isVisible=true
                    group.isVisible=true
                }



            } }
    }



    fun addgroup(){
        val retrofit= ServiceBuilder.buildService(ApiInterface::class.java)
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASEURL)
            .build()
            .create(ApiInterface::class.java)

        retrofitBuilder.getgroup().enqueue(
            object: Callback<groups> {

                override fun onResponse(call: Call<groups>, response: Response<groups>) {
                    val resp = response.body()
                    Log.d("TAG", "${resp}")
                    var x=0

                    if (resp!!.size!=0) {
                        var z= resp!![(resp.size)-1]
                        x=z.sno
                    }
                    else{
                        x=0
                    }
                    val gname:TextView=findViewById(R.id.groupheading)
                    val guser:TextView=findViewById(R.id.gname)

                    val obj=groupsItem(name,gname.text.toString(),guser.text.toString(),x+1)
                    Log.d("ag1",guser.text.toString() )
                    Log.d("ag2",name )
                    Log.d("ag3",gname.text.toString())
                    Log.d("ag4",x.toString() )

                    retrofit.addgroup(obj).enqueue(
                        object: Callback<output> {
                            override fun onResponse(call: Call<output>, response: Response<output>) {
                                val resp = response.body()?.output
                                var string=resp.toString()
                                Toast.makeText(this@addgroupActivity,"${resp}", Toast.LENGTH_SHORT).show()
                                Log.d("sg1",string )
                                val guser:TextView=findViewById(R.id.gname)

                                guser.text=""
                                if(string=="success"){
                                   i=i+1
                                }


                            }
                            override fun onFailure(call: Call<output>, t: Throwable) {
                                Toast.makeText(this@addgroupActivity,"transaction addition failed", Toast.LENGTH_SHORT).show()
                                Log.d("TAG",t.localizedMessage )
                            }
                        }
                    )

                }
                override fun onFailure(call: Call<groups>, t: Throwable) {
                    Toast.makeText(this@addgroupActivity,t.localizedMessage, Toast.LENGTH_SHORT).show()
                    Log.d("TAG",t.localizedMessage )

                }}
        )





    }
}


