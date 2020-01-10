package com.tugasbesarkotlin4.coffeedelivery

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tugasbesarkotlin4.coffeedelivery.Admin.Activity.DasboardAdmin
import com.tugasbesarkotlin4.coffeedelivery.Customer.Activity.DasboardCustomer
import kotlinx.android.synthetic.main.sign_in.*
import java.util.HashMap

class SignIn : AppCompatActivity() {

    internal var firebaseUser: FirebaseUser? = null

    override fun onStart() {
        super.onStart()

        firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null){
            if (firebaseUser!!.email == "admin@gmail.com" ){
                startActivity(Intent(this, DasboardAdmin::class.java))
                finish()
            }else{
                startActivity(Intent(this, DasboardCustomer::class.java))
                finish()
            }
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        val exit = Intent(Intent.ACTION_MAIN)
        exit.addCategory(Intent.CATEGORY_HOME)
        exit.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(exit)
        finish()
    }

    internal lateinit var auth: FirebaseAuth
    internal lateinit var reference: DatabaseReference
    lateinit var animationDrawable: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_in)

        animationDrawable = relLay.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(2000)
        animationDrawable.start()

        val animation2 = AnimationUtils.loadAnimation(this, R.anim.slide_left)
        logostar.startAnimation(animation2)

        auth = FirebaseAuth.getInstance()

        tv_sign_in.setOnClickListener {
            val email = et_email_in.text.toString()
            val password = et_password_in.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }else if (password.length < 6){
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            }else{
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                if (email == "admin@gmail.com"){
                                    val animation = AnimationUtils.loadAnimation(this, R.anim.slide_right)
                                    logostar.startAnimation(animation)
                                    Handler().postDelayed({
                                    startActivity(Intent(this, DasboardAdmin::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                                    finish()},2000)
                                }else{
                                    val animation = AnimationUtils.loadAnimation(this, R.anim.slide_right)
                                    logostar.startAnimation(animation)
                                    Handler().postDelayed({
                                    startActivity(Intent(this, DasboardCustomer::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                                    finish()},2000)
                                }
                            }else{
                                Toast.makeText(
                                    this,
                                    "Authentication Failed!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }

            }

        tv_forgot_password.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
        }

        tv_sign_up.setOnClickListener {
            val username = et_username_up.text.toString()
            val email = et_email_up.text.toString()
            val password = et_password_up.text.toString()

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            }else if (password.length < 6){
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            }else{
                signup(username, email, password)
            }
        }

        tv_go_sign_up.setOnClickListener {
            relLay_up.visibility = View.VISIBLE
            relLay_in.visibility = View.GONE
        }

        tv_go_sign_in.setOnClickListener {
            relLay_up.visibility = View.GONE
            relLay_in.visibility = View.VISIBLE
        }

    }

    private fun signup(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val firebaseUser = auth.currentUser
                    val userid = firebaseUser?.uid

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid!!)

                    val hashMap = HashMap<String, String>()
                    hashMap.put("id", userid)
                    hashMap.put("username", username)
                    hashMap.put("imageUrl", "default")

                    reference.setValue(hashMap).addOnCompleteListener {
                        if (it.isSuccessful){
                            val animation4 = AnimationUtils.loadAnimation(this, R.anim.slide_right)
                            logostar.startAnimation(animation4)
                            Handler().postDelayed({
                            startActivity(Intent(this, DasboardCustomer::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                            finish()},2000)
                        }
                    }
                }else{
                    Toast.makeText(
                        this,
                        "You can't register with this email or password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
