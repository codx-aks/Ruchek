package com.example.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val tranList:ArrayList<transhow>): RecyclerView.Adapter<MyAdapter.MyViewHolder>( ){
    private lateinit var mListener:onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position:Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener=listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder{
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return MyViewHolder(itemView,mListener)
    }

    override fun getItemCount(): Int {
        return tranList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=tranList[position]
        holder.too.text=currentItem.To
        holder.amount.text= currentItem.Amount.toString()
        holder.status.text=currentItem.Status
    }
    class MyViewHolder(itemView: View,listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val too: TextView =itemView.findViewById(R.id.gn)
        val amount: TextView =itemView.findViewById(R.id.gp)
        val status: TextView =itemView.findViewById(R.id.status)
        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
                status.text="settled"
            }
        }

    }
}