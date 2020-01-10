package com.tugasbesarkotlin4.coffeedelivery.Customer.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tugasbesarkotlin4.coffeedelivery.Constant
import com.tugasbesarkotlin4.coffeedelivery.R
import kotlinx.android.synthetic.main.activity_quantity.*
import java.util.HashMap

class Quantity : AppCompatActivity() {

    private var item = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quantity)

        val menuid = intent.getStringExtra(Constant.KEY_ID_MENU)
        val name = intent.getStringExtra(Constant.KEY_NAME)
        val price2 = intent.getStringExtra(Constant.KEY_PRICE)
        val image = intent.getStringExtra(Constant.KEY_IMAGE)

        Glide.with(this)
            .asBitmap()
            .load(image)
            .into(img_product)

        textView1.text = name
        textView2.text = price2
        quantity.text = "1"
        total.text = price2

        increment_button.setOnClickListener {
            val price = price2.toInt()
            increment(price)
        }

        decrement_button.setOnClickListener {
            val price = price2.toInt()
            decrement(price)
        }

        btn_add_cart.setOnClickListener {
            if(quantity.text.toString()=="0"){
                Toast.makeText(this, "Masukkan Quantity",Toast.LENGTH_SHORT).show()
            }else{
                val itemBarang = quantity.text.toString()
                val totalHarga = total.text.toString()

                val id_user = FirebaseAuth.getInstance().currentUser?.uid

                val db_tableChart = FirebaseDatabase.getInstance().getReference("Chart").push()
                val db_tableChartAdmin = FirebaseDatabase.getInstance().getReference("ChartAdmin").push()

                val hashMap = HashMap<String, String>()
                hashMap.put("user_id", id_user!!)
                hashMap.put("menuid", menuid)
                hashMap.put("name", name)
                hashMap.put("quantity", itemBarang)
                hashMap.put("total", totalHarga)

                db_tableChartAdmin.setValue(hashMap).addOnCompleteListener {

                }

                db_tableChart.setValue(hashMap).addOnCompleteListener {
                    Toast.makeText(this, "Barang ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun increment(price: Int) {
        item++
        quantity.text = Integer.toString(item)

        total.text = Integer.toString(sumOfProduct(price))
    }

    private fun decrement(price: Int) {
        if (item > 0) {
            item--

        }

        quantity.text = Integer.toString(item)

        total.text = Integer.toString(sumOfProduct(price))
    }

    private fun sumOfProduct(price: Int): Int {
        return item * price
    }
}
