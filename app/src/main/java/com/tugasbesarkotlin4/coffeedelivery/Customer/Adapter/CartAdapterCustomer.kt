package com.tugasbesarkotlin4.coffeedelivery.Customer.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tugasbesarkotlin4.coffeedelivery.Customer.Model.Chart
import com.tugasbesarkotlin4.coffeedelivery.R

class CartAdapterCustomer(val cartList: List<Chart>, val context: Context) : RecyclerView.Adapter<CartAdapterCustomer.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chart, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int
    {
        return cartList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int)
    {
        holder.name.text = cartList[position].name
        holder.quantity.text = cartList[position].quantity
        holder.price.text = cartList[position].total

    }


    // holder class
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById(R.id.tv_name) as TextView
        val quantity = itemView.findViewById(R.id.tv_quantity) as TextView
        val price = itemView.findViewById(R.id.tv_price) as TextView
    }
}