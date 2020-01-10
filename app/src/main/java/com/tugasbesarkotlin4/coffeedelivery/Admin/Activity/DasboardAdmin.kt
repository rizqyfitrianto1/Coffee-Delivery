package com.tugasbesarkotlin4.coffeedelivery.Admin.Activity

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.tugasbesarkotlin4.coffeedelivery.Admin.Model.Users
import com.tugasbesarkotlin4.coffeedelivery.R
import com.tugasbesarkotlin4.coffeedelivery.SignIn
import kotlinx.android.synthetic.main.activity_dasboard_admin.*
import kotlinx.android.synthetic.main.activity_dasboard_admin.img_profile
import kotlinx.android.synthetic.main.activity_dasboard_admin.logostar
import kotlinx.android.synthetic.main.activity_dasboard_admin.relLay
import kotlinx.android.synthetic.main.activity_dasboard_admin.tv_username

class DasboardAdmin : AppCompatActivity() {

    internal var firebaseUser: FirebaseUser? = null
    internal lateinit var reference: DatabaseReference

    lateinit var animationDrawable: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dasboard_admin)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        animationDrawable = relLay.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(2000)
        animationDrawable.start()

        val animation2 = AnimationUtils.loadAnimation(this, R.anim.slide_left)
        logostar.startAnimation(animation2)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<Users>(Users::class.java)
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

        btn_menu.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        btn_list_order.setOnClickListener {
            startActivity(Intent(this, ListOrder::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_admin, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(
                    Intent(
                        this@DasboardAdmin,
                        SignIn::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                return true
            }
        }

        return false
    }
}
