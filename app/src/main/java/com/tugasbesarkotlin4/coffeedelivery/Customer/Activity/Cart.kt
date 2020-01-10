package com.tugasbesarkotlin4.coffeedelivery.Customer.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.tugasbesarkotlin4.coffeedelivery.Constant
import com.tugasbesarkotlin4.coffeedelivery.Customer.Adapter.CartAdapterCustomer
import com.tugasbesarkotlin4.coffeedelivery.Customer.Model.Chart
import com.tugasbesarkotlin4.coffeedelivery.R
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_dasboard_customer.recyclerview

class Cart : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var cartList: MutableList<Chart>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?

        cartList = mutableListOf()
        LoadData()

        btn_checkout.setOnClickListener {
            val totalChart = total_price.text.toString()

            val intent = Intent(this, ConfirmOrder::class.java)
            intent.putExtra(Constant.KEY_TOTAL_CHART, totalChart)
           startActivity(intent)
        }
    }

    private fun LoadData()
    {
        val akunUser = FirebaseAuth.getInstance().currentUser?.uid
        val databaseRef = FirebaseDatabase.getInstance().getReference("Chart").orderByChild("user_id").equalTo(akunUser)

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError)
            {
                Toast.makeText(applicationContext,"Error Encounter Due to "+databaseError.message, Toast.LENGTH_LONG).show()/**/

            }

            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                var tt_quantity = 0
                var tt_price = 0
                if (dataSnapshot.exists())
                {//before fetch we have clear the list not to show duplicate value
                    cartList.clear()
                    // fetch data & add to list
                    for (data in dataSnapshot.children)
                    {
                        val std = data.getValue(Chart::class.java)
                        cartList.add(std!!)

                        val t_quantity = data.getValue(Chart::class.java)?.quantity?.toInt()
                        tt_quantity += t_quantity!!

                        val t_price = data.getValue(Chart::class.java)?.total?.toInt()
                        tt_price += t_price!!
                    }

                    val adapter = CartAdapterCustomer(cartList, this@Cart)
                    recyclerview.adapter = adapter
                    adapter.notifyDataSetChanged()

                    total_quantity.text = tt_quantity.toString()
                    total_price.text = tt_price.toString()

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
