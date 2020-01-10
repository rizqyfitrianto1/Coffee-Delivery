package com.tugasbesarkotlin4.coffeedelivery.Admin.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tugasbesarkotlin4.coffeedelivery.Admin.Adapter.ListOrderAdapter
import com.tugasbesarkotlin4.coffeedelivery.Customer.Adapter.CartAdapterCustomer
import com.tugasbesarkotlin4.coffeedelivery.Customer.Adapter.MenuAdapterCustomer
import com.tugasbesarkotlin4.coffeedelivery.Customer.Model.Chart
import com.tugasbesarkotlin4.coffeedelivery.Customer.Model.Order
import com.tugasbesarkotlin4.coffeedelivery.R
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_dasboard_customer.*
import kotlinx.android.synthetic.main.activity_dasboard_customer.recyclerview

class ListOrder : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var databaseRef: DatabaseReference
    lateinit var orderList: MutableList<Order>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_order)

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        databaseRef = FirebaseDatabase.getInstance().getReference("Order")
        orderList = mutableListOf()
        LoadData()
    }

    private fun LoadData()
    {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError)
            {
                Toast.makeText(applicationContext,"Error Encounter Due to "+databaseError.message, Toast.LENGTH_LONG).show()/**/

            }

            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                if (dataSnapshot.exists())
                {//before fetch we have clear the list not to show duplicate value
                    orderList.clear()
                    // fetch data & add to list
                    for (data in dataSnapshot.children)
                    {
                        val std = data.getValue(Order::class.java)
                        orderList.add(std!!)
                    }

                    val adapter = ListOrderAdapter(orderList, this@ListOrder)
                    recyclerview.adapter = adapter
                    adapter.notifyDataSetChanged()

                }
                else
                {
                    // if no data found or you can check specefici child value exist or not here
                    Toast.makeText(applicationContext,"No data Found", Toast.LENGTH_LONG).show()
                }

            }

        })
    }
}
