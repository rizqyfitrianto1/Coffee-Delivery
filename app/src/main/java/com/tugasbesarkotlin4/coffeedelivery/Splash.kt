package com.tugasbesarkotlin4.coffeedelivery

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import kotlin.Pair

class Splash : AppCompatActivity() {

    private var imageView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        imageView = findViewById<View>(R.id.logostar) as ImageView?
        val myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition)
        imageView!!.startAnimation(myanim)

        val intent = Intent(this, SignIn::class.java)

        val timer = object : Thread() {
            override fun run() = try {
                sleep(3000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } finally {
                startActivity(intent)
                finish()
            }
        }
        timer.start()
    }
}
