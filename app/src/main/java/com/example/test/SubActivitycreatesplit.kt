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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SubActivitycreatesplit : AppCompatActivity() {
    private lateinit var name:String
    private lateinit var sname:MutableList<String>
    private var Amount:Int =0
    private var No:Int=0
    private var uamount=0F
    private var i=0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        setContentView(R.layout.activity_sub_activitycreatesplit)
        name= intent.getStringExtra("username").toString()
        val user: TextView =findViewById(R.id.user)
        user.text=name
        val sback: Button =findViewById(R.id.sback)
        sback.setOnClickListener {
            val intent = Intent(this,MainActivitysplit::class.java)

            intent.putExtra("username",name)
            startActivity(intent)
        }
        val stitleget:TextView=findViewById(R.id.stitleget)
        val samountget:TextView=findViewById(R.id.samountget)
        val speopleget:TextView=findViewById(R.id.speopleget)
        val split:Button=findViewById(R.id.splitb)
        val splitname:TextView=findViewById(R.id.splitname)
        val splitnext:Button=findViewById(R.id.addp)
        val splitheading:TextView=findViewById(R.id.splitheading)
        sname= mutableListOf()
        split.setOnClickListener {
            if(stitleget.text!=""){
                if(samountget.text!=""){
                    if(speopleget.text!=""){
                        splitheading.text=stitleget.text.toString()
                        Amount=samountget.text.toString().toInt()
                        No=speopleget.text.toString().toInt()
                        splitname.isVisible=true
                        splitnext.isVisible=true
                        splitheading.isVisible=true
                        stitleget.isVisible=false
                        samountget.isVisible=false
                        speopleget.isVisible=false
                        split.isVisible=false
                        stitleget.text=""
                        samountget.text=""
                        speopleget.text=""
                        uamount= (Amount/No).toFloat()
                    }
                } } }

        splitnext.setOnClickListener {
            if(i!=No){
                if(splitname.text!=""){
                    sname.add(splitname.text.toString())

                    addsplit()

                }

            }


        }

    }
    fun addsplit(){
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
                    val uname:TextView=findViewById(R.id.splitname)


                    var w="owes you"

                    val obj=transactionsItem(uamount.toInt(),name,sname[sname.size-1],w,y+1,x+1)
                    Log.d("1",uamount.toString())
                    Log.d("2",name )
                    Log.d("3",sname[sname.size-1])
                    Log.d("4",w )
                    Log.d("5",y.toString() )
                    Log.d("6",x.toString() )
                    retrofit.addtran(obj).enqueue(
                        object: Callback<output> {
                            override fun onResponse(call: Call<output>, response: Response<output>) {
                                val resp = response.body()?.output
                                Toast.makeText(this@SubActivitycreatesplit,"${resp}", Toast.LENGTH_SHORT).show()

                            }
                            override fun onFailure(call: Call<output>, t: Throwable) {
                                Toast.makeText(this@SubActivitycreatesplit,"transaction addition failed", Toast.LENGTH_SHORT).show()
                                Log.d("TAG",t.localizedMessage )
                            } })
                }
                override fun onFailure(call: Call<transactions>, t: Throwable) {
                    Toast.makeText(this@SubActivitycreatesplit,t.localizedMessage,Toast.LENGTH_SHORT).show()
                    Log.d("TAG",t.localizedMessage )
                }})



        retrofitBuilder.getsplits().enqueue(
            object: Callback<splits> {

                override fun onResponse(call: Call<splits>, response: Response<splits>) {
                    val respon = response.body()
                    Log.d("TAG", "${respon}")
                    var x=0

                    if (respon!!.size!=0) {
                        var z= respon[(respon.size)-1]
                        x=z.no

                    }
                    else{
                        x=0
                    }
                    val uname:TextView=findViewById(R.id.splitname)
                    val utitle:TextView=findViewById(R.id.splitheading)


                    val obj=splitsItem(name,uamount.toInt(),sname[sname.size-1],x+1,utitle.text.toString())
                    Log.d("1",uamount.toString())
                    Log.d("2",name )
                    Log.d("3",sname[sname.size-1])
                    Log.d("5",x.toString() )
                    retrofit.addsplit(obj).enqueue(
                        object: Callback<output> {
                            override fun onResponse(call: Call<output>, response: Response<output>) {
                                val resp = response.body()?.output
                                Toast.makeText(this@SubActivitycreatesplit,"${resp}", Toast.LENGTH_SHORT).show()
                                if(resp=="split added successfully"){
                                    i=i+1
                                }

                            }
                            override fun onFailure(call: Call<output>, t: Throwable) {
                                Toast.makeText(this@SubActivitycreatesplit,"split addition failed", Toast.LENGTH_SHORT).show()
                                Log.d("TAG",t.localizedMessage )
                            } })
                }
                override fun onFailure(call: Call<splits>, t: Throwable) {
                    Toast.makeText(this@SubActivitycreatesplit,t.localizedMessage,Toast.LENGTH_SHORT).show()
                    Log.d("TAG",t.localizedMessage )
                }})
        val uname:TextView=findViewById(R.id.splitname)
        uname.text=""
        val splitnext:Button=findViewById(R.id.addp)

        if(i==No-2){
            splitnext.text="CREATE SPLIT"
        }
        val stitleget:TextView=findViewById(R.id.stitleget)
        val samountget:TextView=findViewById(R.id.samountget)
        val speopleget:TextView=findViewById(R.id.speopleget)
        val split:Button=findViewById(R.id.splitb)
        val splitname:TextView=findViewById(R.id.splitname)

        val splitheading:TextView=findViewById(R.id.splitheading)

        if(i==No-1){
            splitname.text = ""
            splitname.isVisible=false
            splitnext.isVisible=false
            splitheading.isVisible=false
            stitleget.isVisible=true
            samountget.isVisible=true
            speopleget.isVisible=true
            split.isVisible=true
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "split created for ${splitheading.text.toString()} , you have to pay rs: $uamount"
            )
            intent.type="text/plain"
            startActivity(Intent.createChooser(intent,"Share to:"))

        }
    }
}