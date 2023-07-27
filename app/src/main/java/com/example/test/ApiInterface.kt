package com.example.test

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query


interface ApiInterface {

    @POST("register")
    fun register(@Body requestModel: input):Call<output>
    @POST("login")
    fun login(@Body requestModel: input):Call<token>
    @GET("transactions")
    fun gettransactions():Call<transactions>
    @POST("addtran")
    fun addtran(@Body requestModel: transactionsItem):Call<output>
    @PUT("settle")
    fun tosettle(@Query("tno") tno:Int):Call<output>
    @GET("getsplit")
    fun getsplits():Call<splits>
    @POST("addsplit")
    fun addsplit(@Body requestModel: splitsItem):Call<output>
    @GET("getgroup")
    fun getgroup():Call<groups>
    @POST("addgroup")
    fun addgroup(@Body requestModel: groupsItem):Call<output>
    @PUT("snamechange")
    fun splitedit(@Body requestModel: splitsItem):Call<output>

}