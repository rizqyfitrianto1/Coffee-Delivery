package com.tugasbesarkotlin4.coffeedelivery.Admin.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tugasbesarkotlin4.coffeedelivery.Admin.Activity.DetailOrder
import com.tugasbesarkotlin4.coffeedelivery.Constant
import com.tugasbesarkotlin4.coffeedelivery.Customer.Model.Order
import com.tugasbesarkotlin4.coffeedelivery.R

class ListOrderAdapter(val orderList: List<Order>, val context: Context) : RecyclerView.Adapter<ListOrderAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int
    {
        return orderList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int)
    {
        holder.orderid.text = orderList[position].orderid
        holder.name.text = orderList[position].name
        holder.no_telp.text = orderList[position].no_telp
        holder.alamat.text = orderList[position].alamat
        holder.total.text = orderList[position].total

        holder.lihatpesanan.setOnClickListener {
            val user_id = orderList[position].user_id

            val intent = Intent(context, DetailOrder::class.java)
            intent.putExtra(Constant.KEY_ID_USER, user_id)
            context.startActivity(intent)
        }
    }


    // holder class
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val orderid = itemView.findViewById(R.id.orderid) as TextView
        val name = itemView.findViewById(R.id.name) as TextView
        val no_telp = itemView.findViewById(R.id.no_telp) as TextView
        val alamat = itemView.findViewById(R.id.alamat) as TextView
        val total = itemView.findViewById(R.id.total) as TextView

        val lihatpesanan = itemView.findViewById(R.id.lihatpesanan) as Button
    }
}