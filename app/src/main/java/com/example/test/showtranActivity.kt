package com.example.test

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Call
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Callback
import retrofit2.Response


class showtranActivity : AppCompatActivity() {

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList: ArrayList<transhow>
    lateinit var too: MutableList<String>
    lateinit var amount: MutableList<Int>
    lateinit var status: MutableList<String>
    lateinit var tno: MutableList<Int>
    private lateinit var name: String
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        setContentView(R.layout.activity_main)
        val back: Button=findViewById(R.id.button)
        back.setOnClickListener {
            val intent = Intent(this,MainActivitytransaction::class.java)
            intent.putExtra("username",name)
            startActivity(intent)
        }
        newArrayList = arrayListOf<transhow>()
        name = intent.getStringExtra("username").toString()
        newRecyclerView = findViewById(R.id.recyclerView)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)
        newRecyclerView = findViewById(R.id.recyclerView)
        val adapter=MyAdapter(newArrayList)
        newRecyclerView.adapter =adapter


        gettransactions()
    }
    private fun gettransactions() {
        val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
        retrofit.gettransactions().enqueue(
            object : Callback<transactions> {
                override fun onResponse(
                    call: Call<transactions>,
                    response: Response<transactions>
                ) {
                    val resp = response.body()
                    Log.d("TAG", "${resp}")
                    too = mutableListOf()
                    amount = mutableListOf()
                    status = mutableListOf()
                    tno = mutableListOf()

                    for (z in resp!!) {
                        if (z.From == name) {
                            too.add(z.To)
                            amount.add(z.Amount)
                            status.add(z.status)
                            tno.add(z.tranid)
                        }
                    }
                    Log.d("TAG", "${too}")
                    newRecyclerView = findViewById(R.id.recyclerView)
                    newArrayList = arrayListOf<transhow>()
                    getUserdata()
                }

                override fun onFailure(call: Call<transactions>, t: Throwable) {

                    Log.d("TAG", t.localizedMessage)

                }
            }
        )
    }

    private fun getUserdata() {
        for (i in too.indices) {
            val tran = transhow(too[i], amount[i], status[i])
            newArrayList.add(tran)
        }

        newRecyclerView = findViewById(R.id.recyclerView)
        val adapter=MyAdapter(newArrayList)
        newRecyclerView.adapter =adapter

        adapter.setOnItemClickListener(listener = object: MyAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                Toast.makeText(this@showtranActivity,"transaction ${position} settled",Toast.LENGTH_SHORT).show()

                val retrofit = ServiceBuilder.buildService(ApiInterface::class.java)
                retrofit.tosettle(tno[position]).enqueue(
                    object : Callback<output> {
                        override fun onResponse(
                            call: Call<output>,
                            response: Response<output>
                        ) {
                            val respo = response.body()
                            Log.d("settle resp", "${respo}")

                        }

                        override fun onFailure(call: Call<output>, t: Throwable) {

                            Log.d("settle fail", t.localizedMessage)

                        }
                    }
                )
            } })


    }
   }
