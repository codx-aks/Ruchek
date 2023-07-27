package com.example.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapterS(private val splitList:ArrayList<splitshow>): RecyclerView.Adapter<MyAdapterS.MyViewHolder>( ){
    private lateinit var mListener:onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position:Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener=listener

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapterS.MyViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return MyAdapterS.MyViewHolder(itemView,mListener)
    }
    override fun getItemCount(): Int {
        return splitList.size
    }
    override fun onBindViewHolder(holder: MyAdapterS.MyViewHolder, position: Int) {
        val currentItem=splitList[position]
        holder.too.text= currentItem.id.toString()
        holder.amount.text= currentItem.To
        holder.status.text=currentItem.Amount.toString()
    }
    class MyViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val too: TextView =itemView.findViewById(R.id.gn)
        val amount: TextView =itemView.findViewById(R.id.gp)
        val status: TextView =itemView.findViewById(R.id.status)
        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)

            }
        }
    }


}