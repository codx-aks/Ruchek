package com.example.test

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapterC(private val groupList:ArrayList<groupshow>): RecyclerView.Adapter<MyAdapterC.MyViewHolder>( ){
    private lateinit var mListener:onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position:Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener=listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapterC.MyViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.grp_item,parent,false)
        return MyAdapterC.MyViewHolder(itemView,mListener)
    }
    override fun getItemCount(): Int {
        return groupList.size
    }
    override fun onBindViewHolder(holder: MyAdapterC.MyViewHolder, position: Int) {
        val currentItem=groupList[position]
        holder.gn.text= currentItem.groupname
        holder.gp.text= currentItem.username
    }
    class MyViewHolder(itemView: View, listener: onItemClickListener): RecyclerView.ViewHolder(itemView){
        val gn: TextView =itemView.findViewById(R.id.gn)
        val gp: TextView =itemView.findViewById(R.id.gp)
        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}