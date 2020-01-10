package com.tugasbesarkotlin4.coffeedelivery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPasswordActivity : AppCompatActivity() {

    internal lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        firebaseAuth = FirebaseAuth.getInstance()

        btn_reset.setOnClickListener {
            val email = send_email.text.toString()

            if (email.equals("")){
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
            }else{
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(this, "Please check your Email", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, SignIn::class.java))
                        finish()
                    }else{
                        val error = it.exception!!.message
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}
