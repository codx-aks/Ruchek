package com.example.test

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Build.VERSION_CODES.P
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class groupsplitActivity : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<groupshow>
    private lateinit var newtranList: ArrayList<transactionsItem>
    private lateinit var newsplitList: ArrayList<splitsItem>
    lateinit var groups: MutableList<String>
    lateinit var people: MutableList<String>
    lateinit var gp:MutableList<String>
    lateinit var group: MutableList<String>

    private lateinit var name: String
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        setContentView(R.layout.activity_groupsplit)
        val back: Button =findViewById(R.id.button)
        back.setOnClickListener {
            val intent = Intent(this,MainActivitysplit::class.java)
            intent.putExtra("username",name)
            startActivity(intent)
        }
        newArrayList = arrayListOf<groupshow>()
        name = intent.getStringExtra("username").toString()
        newRecyclerView = findViewById(R.id.recyclerView)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)
        newRecyclerView = findViewById(R.id.recyclerView)
        val adapter=MyAdapterC(newArrayList)
        newRecyclerView.adapter =adapter


        getgroups()
    }
    private fun getgroups() {
        val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
        retrofit.getgroup().enqueue(
            object : Callback<groups> {
                override fun onResponse(
                    call: Call<groups>,
                    response: Response<groups>
                ) {
                    val resp = response.body()
                    Log.d("TAG", "${resp}")

                    groups = mutableListOf()
                    group = mutableListOf()
                    people = mutableListOf()
                    gp= mutableListOf()

                    var x=0
                    var y=0
                    var j=0
                    for (z in resp!!) {
                        if (z.Creator == name) {


                            if(z.Groupname!=resp[y].Groupname){
                                x=0
                                y=j
                            }
                            if(x==0){
                                groups.add(z.Groupname)
                            }
                            else{
                               groups.add("")
                            }
                            people.add(z.People)
                            group.add(z.Groupname)
                            x=x+1
                        }
                        j=j+1
                    }
                    Log.d("TAG", "${groups}")
                    newRecyclerView = findViewById(R.id.recyclerView)
                    newArrayList = arrayListOf<groupshow>()
                    newtranList = arrayListOf<transactionsItem>()
                    newsplitList= arrayListOf<splitsItem>()
                    getUserdata()
                }

                override fun onFailure(call: Call<groups>, t: Throwable) {

                    Log.d("TAG", t.localizedMessage)

                } })
    }
    private fun getUserdata() {
        for (i in groups.indices) {

            val grpz = groupshow(groups[i],people[i])
            newArrayList.add(grpz)


            newRecyclerView = findViewById(R.id.recyclerView)
            val adapter=MyAdapterC(newArrayList)
            newRecyclerView.adapter =adapter

            adapter.setOnItemClickListener(listener = object: MyAdapterC.onItemClickListener{
                override fun onItemClick(position: Int) {
                    dialog(position,group[position])
                    Toast.makeText(this@groupsplitActivity,"split added",
                        Toast.LENGTH_SHORT).show()

                } })
        }
    }
    @SuppressLint("MissingInflatedId")
    fun dialog(position: Int, grpname: String) {
        Log.d("TAG", "${grpname}")
        val dialogBinding1 = layoutInflater.inflate(R.layout.grpsp, null)
        val mydialog1 = Dialog(this)
        mydialog1.setContentView(dialogBinding1)
        mydialog1.setCancelable(true)

        Toast.makeText(this@groupsplitActivity,"fill up every space , if no changes type out the previous name",
            Toast.LENGTH_SHORT).show()


        var ganew=dialogBinding1.findViewById<TextView>(R.id.ganew)
        var sub=dialogBinding1.findViewById<TextView>(R.id.sub)
        val gtitle=dialogBinding1.findViewById<TextView>(R.id.splitgn)

        sub.setOnClickListener {

                    if(ganew.text!=""){
                        if(gtitle.text!=""){
                        val retrofit1 = ServiceBuilder.buildService(ApiInterface::class.java)
                        retrofit1.getgroup().enqueue(
                            object : Callback<groups> {
                                override fun onResponse(
                                    call: Call<groups>,
                                    response: Response<groups>
                                ) {
                                    val resp = response.body()
                                    Log.d("TAG", "${resp}")

                                    for (f in resp!!){
                                        if(f.Groupname==grpname){
                                            gp.add(f.People)
                                        }
                                    }
                                    Log.d("people name", "${gp}")
                                    for (i in gp.indices) {
                                        Log.d("tranlistzz", "${i}")
                                        val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)

                                        val retrofitBuilder = Retrofit.Builder()
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .baseUrl(BASEURL)
                                            .build()
                                            .create(ApiInterface::class.java)

                                        retrofitBuilder.gettransactions().enqueue(
                                            object : Callback<transactions> {

                                                override fun onResponse(
                                                    call: Call<transactions>,
                                                    response: Response<transactions>
                                                ) {
                                                    val resp = response.body()
                                                    Log.d("tranlist", "${resp}")
                                                    var x = 0
                                                    var y = 0
                                                    if (resp != null) {
                                                        var z = resp[(resp.size) - 1]
                                                        x = z.tranno
                                                        y = z.tranid
                                                    } else {
                                                        x = 0
                                                        y = 0
                                                    }
                                                    val obj = transactionsItem(
                                                        ganew.text.toString().toInt(),
                                                        name,
                                                        gp[i],
                                                        "owes you",
                                                        y + i+1,
                                                        x + 2*i+1
                                                    )
                                                    newtranList.add(obj)

                                                    retrofitBuilder.getsplits().enqueue(
                                                        object : Callback<splits> {

                                                            override fun onResponse(
                                                                call: Call<splits>,
                                                                response: Response<splits>
                                                            ) {
                                                                val respon = response.body()
                                                                Log.d("splits list", "${respon}")
                                                                var x = 0

                                                                if (respon!!.size != 0) {
                                                                    var z = respon[(respon.size) - 1]
                                                                    x = z.no

                                                                } else {
                                                                    x = 0
                                                                }




                                                                val obj1 = splitsItem(
                                                                    name,
                                                                    ganew.text.toString().toInt(),
                                                                    gp[i],
                                                                    x + 1+i,
                                                                    gtitle.text.toString()
                                                                )
                                                                newsplitList.add(obj1)
                                                                Log.d("gss1", ganew.text.toString())
                                                                Log.d("gss2", name)
                                                                Log.d("gss3", gp[i])
                                                                Log.d("gss5", x.toString())
                                                                Log.d("newtran", "${newtranList}")
                                                                Log.d("newsplit", "${newsplitList}")

                                                                if(newtranList.size==gp.size){
                                                                    if(newsplitList.size==gp.size){
                                                                        addition()
                                                                    }}
                                                                else{
                                                                    Log.d("newt", "${newtranList.size}")
                                                                    Log.d("newgp", "${gp.size}")
                                                                }

                                                            }

                                                            override fun onFailure(call: Call<splits>, t: Throwable) {
                                                                Toast.makeText(
                                                                    this@groupsplitActivity,
                                                                    t.localizedMessage,
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                                Log.d("sfail", t.localizedMessage)
                                                            }
                                                        })
                                                }

                                                override fun onFailure(call: Call<transactions>, t: Throwable) {
                                                    Toast.makeText(
                                                        this@groupsplitActivity,
                                                        t.localizedMessage,
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    Log.d("tfail", t.localizedMessage)
                                                }
                                            })




                                    }
                                }
                                override fun onFailure(call: Call<groups>, t: Throwable) {
                                    Log.d("TAG", t.localizedMessage)
                                } })
                        name = intent.getStringExtra("username").toString()
            mydialog1.dismiss()
                        }}
        }
        mydialog1.show()}
    fun addition(){
        Log.d("newtranlast", "${newtranList}")
        Log.d("newsplitlast", "${newsplitList}")
        for (n in newtranList){
            val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
            retrofit.addtran(n).enqueue(
                object: Callback<output> {
                    override fun onResponse(call: Call<output>, response: Response<output>) {
                        val resp = response.body()?.output
                        var string=resp.toString()
                        Log.d("s1",string )

                    }
                    override fun onFailure(call: Call<output>, t: Throwable) {
                        Toast.makeText(this@groupsplitActivity,"transaction addition failed", Toast.LENGTH_SHORT).show()
                        Log.d("TAG",t.localizedMessage )
                    }
                }
            )



        }
        for (m in newsplitList){
            val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
            retrofit.addsplit(m).enqueue(
                object: Callback<output> {
                    override fun onResponse(call: Call<output>, response: Response<output>) {
                        val re = response.body()?.output
                        var st=re.toString()
                        Log.d("s1",st)

                    }
                    override fun onFailure(call: Call<output>, t: Throwable) {
                        Toast.makeText(this@groupsplitActivity,"split addition failed", Toast.LENGTH_SHORT).show()
                        Log.d("TAG",t.localizedMessage )
                    }
                }
            )
        }
    }
}
