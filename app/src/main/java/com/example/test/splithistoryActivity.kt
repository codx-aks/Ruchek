package com.example.test

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class splithistoryActivity : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<splitshow>
    lateinit var sno: MutableList<String>
    lateinit var Payment: MutableList<Int>
    lateinit var people: MutableList<String>
    lateinit var no: MutableList<Int>

    private lateinit var name: String
    private var srch=""
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        setContentView(R.layout.activity_splithistory)
        val back: Button =findViewById(R.id.button)
        back.setOnClickListener {
            val intent = Intent(this,MainActivitysplit::class.java)
            intent.putExtra("username",name)
            startActivity(intent)
        }
        newArrayList = arrayListOf<splitshow>()
        name = intent.getStringExtra("username").toString()
        newRecyclerView = findViewById(R.id.recyclerView)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)
        newRecyclerView = findViewById(R.id.recyclerView)
        val adapter=MyAdapterS(newArrayList)
        newRecyclerView.adapter =adapter

        Toast.makeText(this@splithistoryActivity,"Click on empty search bar to display all texts And type in splitname to search for specific data",Toast.LENGTH_LONG).show()
        getsplits()
        val search:TextView=findViewById(R.id.search)
        search.setOnClickListener {
            val intent = Intent(this,splithistoryActivity::class.java)
            intent.putExtra("username",name)
            intent.putExtra("srch",search.text.toString())
            startActivity(intent)
        }
        srch = intent.getStringExtra("srch").toString()
    }
    private fun getsplits() {
        val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
        retrofit.getsplits().enqueue(
            object : Callback<splits> {
                override fun onResponse(
                    call: Call<splits>,
                    response: Response<splits>
                ) {
                    val resp = response.body()
                    Log.d("TAG", "${resp}")
                    sno = mutableListOf()
                    Payment = mutableListOf()
                    people = mutableListOf()
                    no = mutableListOf()

                    var x=0
                    var y=0
                    var j=0
                    val search:TextView=findViewById(R.id.search)
                    for (z in resp!!) {
                        if(srch==""){
                        if (z.Creator == name) {
                            no.add(z.no)

                            Payment.add(z.Payment)
                            if(z.splitname!=resp[y].splitname){
                                x=0
                                y=j
                            }

                            if(x==0){
                                sno.add(z.splitname)
                            }
                            else{
                                sno.add("")
                            }
                            people.add(z.People)
                            x=x+1
                        }
                        j=j+1}
                        else{
                            if (z.Creator == name) {
                                if(z.splitname==srch) {
                                    no.add(z.no)

                                    Payment.add(z.Payment)
                                    if (z.splitname != resp[y].splitname) {
                                        x = 0
                                        y = j
                                    }

                                    if (x == 0) {
                                        sno.add(z.splitname)
                                    } else {
                                        sno.add("")
                                    }
                                    people.add(z.People)
                                    x = x + 1
                                }}
                            j=j+1
                        }
                    }
                    Log.d("TAG", "${sno}")
                    newRecyclerView = findViewById(R.id.recyclerView)
                    newArrayList = arrayListOf<splitshow>()
                    getUserdata()
                }

                override fun onFailure(call: Call<splits>, t: Throwable) {

                    Log.d("TAG", t.localizedMessage)

                } })
    }
    private fun getUserdata() {
        for (i in sno.indices) {

                val spliz = splitshow(sno[i],people[i], Payment[i] )
                newArrayList.add(spliz)
            newRecyclerView = findViewById(R.id.recyclerView)
            val adapter=MyAdapterS(newArrayList)
            newRecyclerView.adapter =adapter

            adapter.setOnItemClickListener(listener = object: MyAdapterS.onItemClickListener{
                override fun onItemClick(position: Int) {
                    dialog(position)
                    Toast.makeText(this@splithistoryActivity,"split ${position} to be edited",Toast.LENGTH_SHORT).show()

                } })
        }


    }
    @SuppressLint("MissingInflatedId")
    fun dialog(position:Int) {
        val dialogBinding1 = layoutInflater.inflate(R.layout.edit, null)
        val mydialog1 = Dialog(this)
        mydialog1.setContentView(dialogBinding1)
        mydialog1.setCancelable(true)

        Toast.makeText(this@splithistoryActivity,"fill up every space , if no changes type out the previous name",Toast.LENGTH_SHORT).show()

        val snnew = dialogBinding1.findViewById<TextView>(R.id.snnew)
        var sunew=dialogBinding1.findViewById<TextView>(R.id.sunew)
        var sanew=dialogBinding1.findViewById<TextView>(R.id.sanew)
        var sub=dialogBinding1.findViewById<TextView>(R.id.sub)


        sub.setOnClickListener {
            if(snnew.text!=""){
                if(sunew.text!=""){
                    if(sanew.text!=""){

                        name = intent.getStringExtra("username").toString()
                        val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
                        val obj=splitsItem(name,sanew.text.toString().toInt(),sunew.text.toString(),no[position],snnew.text.toString())
                        retrofit.splitedit(obj).enqueue(
                            object : Callback<output> {
                                override fun onResponse(
                                    call: Call<output>,
                                    response: Response<output>
                                ) {
                                    val respo = response.body()
                                    Log.d("split edit success", "${respo}")
                                    sanew.text=""
                                    sunew.text=""
                                    snnew.text=""
                                }

                                override fun onFailure(call: Call<output>, t: Throwable) {

                                    Log.d("split edit fail", t.localizedMessage)

                                }
                            }
                        )
                        var san=sanew.text.toString()
                        var sun=sunew.text.toString()
                        var snn=snnew.text.toString()

                    }
                }
            }
            mydialog1.dismiss()
            val intent = Intent(this,splithistoryActivity::class.java)
            intent.putExtra("username",name)
            startActivity(intent)
        }
    mydialog1.show()}
}





