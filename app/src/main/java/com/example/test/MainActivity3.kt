package com.example.test

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.isVisible
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity3 : AppCompatActivity() {
    private lateinit var name:String
    @SuppressLint("MissingInflatedId")
    val CHANNEL_ID = "pickerChannel"
    override fun onCreate(savedInstanceState: Bundle?) {
        name= intent.getStringExtra("username").toString()
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        val user:TextView=findViewById(R.id.user)
        user.text=name
        val tran:TextView=findViewById(R.id.tran)
        val split:TextView=findViewById(R.id.split)
        tran.setOnClickListener {
            val intent = Intent(this, MainActivitytransaction::class.java)
            intent.putExtra("username",name)

            startActivity(intent)
        }
        split.setOnClickListener {
            val intent = Intent(this, MainActivitysplit::class.java)
            intent.putExtra("username",name)

            startActivity(intent)
        }
        gettransactions()

        createNotificationChannel()

        }
    private fun gettransactions(){
        val retrofit= ServiceBuilder.buildService(ApiInterface::class.java)
        retrofit.gettransactions().enqueue(
            object: Callback<transactions> {

                override fun onResponse(call: Call<transactions>, response: Response<transactions>) {
                    val resp = response.body()
                    val lent:TextView=findViewById(R.id.lent)
                    val borrowed:TextView=findViewById(R.id.borrowed)

                    Log.d("TAG", "${resp}")
                    var x=0
                    var y=0
                    for (z in resp!!){
                        if(z.From==name){
                            if(z.status=="owes you"){
                                x=x+z.Amount
                            }
                            else if(z.status=="settle"){
                                y=y+z.Amount
                            }
                        }
                    }
                    lent.text= x.toString()
                    borrowed.text=y.toString()
                    showNotification()
                }
                override fun onFailure(call: Call<transactions>, t: Throwable) {

                    Log.d("TAG",t.localizedMessage )

                }
            }
        )
    }
    fun createNotificationChannel() {
        // Create the notification channel is needed from O OS.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel_name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID,name,importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager:NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification() {
        val lent:TextView=findViewById(R.id.lent)
        val borrowed:TextView=findViewById(R.id.borrowed)
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("Net BALANCE Alert")
            .setContentText("your net pending transaction balance is ${lent.text.toString().toInt()-borrowed.text.toString().toInt()}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            // Use a unique notification ID to update or cancel the notification later
            val notificationId = 1
            notify(notificationId, notificationBuilder.build())
        }
    }




    }

