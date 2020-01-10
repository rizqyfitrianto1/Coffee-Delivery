package com.tugasbesarkotlin4.coffeedelivery.Customer.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tugasbesarkotlin4.coffeedelivery.Admin.Model.Menu
import com.tugasbesarkotlin4.coffeedelivery.Constant
import com.tugasbesarkotlin4.coffeedelivery.Customer.Activity.Quantity
import com.tugasbesarkotlin4.coffeedelivery.R

class MenuAdapterCustomer(val menuList: MutableList<Menu>, val context: Context) : RecyclerView.Adapter<MenuAdapterCustomer.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_layout_customer, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int
    {
        return menuList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int)
    {
        holder.name.text = menuList[position].name
        holder.price.text = menuList[position].price
        Glide.with(context).load(menuList[position].image).into(holder.image)

        // if user click on update icon for  update operation
        holder.itemView.setOnClickListener()
        {
            val menuid = menuList[position].menuid
            val nama_product = menuList[position].name
            val harga_product = menuList[position].price
            val image_product = menuList[position].image

            val intent = Intent(context, Quantity::class.java)
            intent.putExtra(Constant.KEY_ID_MENU, menuid)
            intent.putExtra(Constant.KEY_NAME, nama_product)
            intent.putExtra(Constant.KEY_PRICE, harga_product)
            intent.putExtra(Constant.KEY_IMAGE, image_product)
            context.startActivity(intent)
        }

    }


    // holder class
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById(R.id.textView1) as TextView
        val price = itemView.findViewById(R.id.textView2) as TextView
        val image = itemView.findViewById(R.id.img_product) as ImageView

        // action operation widget
        val pesan = itemView.findViewById(R.id.tv_pesan) as TextView

    }
}