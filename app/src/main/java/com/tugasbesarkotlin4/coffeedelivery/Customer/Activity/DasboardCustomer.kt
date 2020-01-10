package com.tugasbesarkotlin4.coffeedelivery.Customer.Activity

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.tugasbesarkotlin4.coffeedelivery.Admin.Model.Users
import com.tugasbesarkotlin4.coffeedelivery.Customer.Adapter.MenuAdapterCustomer
import com.tugasbesarkotlin4.coffeedelivery.R
import com.tugasbesarkotlin4.coffeedelivery.SignIn
import kotlinx.android.synthetic.main.activity_dasboard_customer.*
import kotlinx.android.synthetic.main.activity_dasboard_customer.logostar
import kotlinx.android.synthetic.main.activity_dasboard_customer.recyclerview
import kotlinx.android.synthetic.main.activity_dasboard_customer.relLay

class DasboardCustomer : AppCompatActivity() {

    internal var firebaseUser: FirebaseUser? = null
    internal lateinit var reference: DatabaseReference

    lateinit var animationDrawable: AnimationDrawable
    lateinit var recyclerView: RecyclerView
    lateinit var databaseRef: DatabaseReference
    lateinit var menuList: MutableList<com.tugasbesarkotlin4.coffeedelivery.Admin.Model.Menu>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dasboard_customer)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        animationDrawable = relLay.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(2000)
        animationDrawable.start()

        val animation2 = AnimationUtils.loadAnimation(this, R.anim.slide_left)
        logostar.startAnimation(animation2)

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<Users>(Users::class.java!!)
                tv_username.text = user!!.username
                if (user.imageUrl == "default") {
                    img_profile.setImageResource(R.mipmap.ic_launcher)
                } else {
                    //chage this
                    Glide.with(applicationContext).load(user.imageUrl).into(img_profile)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        databaseRef = FirebaseDatabase.getInstance().getReference("Menu")
        menuList = mutableListOf()
        LoadData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(
                    Intent(
                        this@DasboardCustomer,
                        SignIn::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )

                return true
            }

            R.id.cart -> {
                startActivity(Intent(this, Cart::class.java))
            }

        }

        return false
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
                    menuList.clear()
                    // fetch data & add to list
                    for (data in dataSnapshot.children)
                    {
                        val std = data.getValue(com.tugasbesarkotlin4.coffeedelivery.Admin.Model.Menu::class.java)
                        menuList.add(std!!)
                    }

                    val adapter = MenuAdapterCustomer(menuList, this@DasboardCustomer)
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
