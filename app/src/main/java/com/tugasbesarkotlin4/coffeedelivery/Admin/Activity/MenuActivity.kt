package com.tugasbesarkotlin4.coffeedelivery.Admin.Activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.tugasbesarkotlin4.coffeedelivery.Admin.Adapter.MenuAdapter
import com.tugasbesarkotlin4.coffeedelivery.Admin.Model.Menu
import com.tugasbesarkotlin4.coffeedelivery.R
import kotlinx.android.synthetic.main.activity_menu.*
import java.util.*

class MenuActivity : AppCompatActivity() {

    lateinit var menu_name: EditText
    lateinit var menu_price: EditText
    lateinit var menu_image: ImageView
    lateinit var savebutton: Button
    lateinit var progressBar: ProgressBar
    lateinit var databaseRef: DatabaseReference
    lateinit var menuList: MutableList<Menu>
    lateinit var recyclerView: RecyclerView

    private var imgPath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // below 2 line is for add seperator line in peritem of reyclerview
//        val itemDecor = DividerItemDecoration(this, VERTICAL)
//        recyclerView.addItemDecoration(itemDecor)

        menu_name = findViewById(R.id.editText);
        menu_price = findViewById(R.id.editText1);
        menu_image = findViewById(R.id.img_product)
        savebutton = findViewById(R.id.savedata);
        progressBar = findViewById(R.id.progressbar)
        databaseRef = FirebaseDatabase.getInstance().getReference("Menu")

        menu_image.setOnClickListener {
            val mImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(mImg,0)
        }

        // initialize mutable list
        menuList = mutableListOf()

        // save data to database
        savebutton.setOnClickListener()
        {
            savedatatoserver()
            progressBar.visibility = View.VISIBLE
        }

        // call load data method in main thread
        LoadData()
    }

    private fun savedatatoserver()
    {
        // get value from edit text & spinner
        val name: String = menu_name.text.toString().trim()
        val price: String = menu_price.text.toString().trim()

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(price))
        {
            val filename = UUID.randomUUID().toString()
            val storageReference = FirebaseStorage.getInstance().getReference("image_product/$filename")
            val databaseReference = FirebaseDatabase.getInstance().getReference("Menu").push()
            val menuid = databaseReference.key

            storageReference.putFile(imgPath!!).addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener {
                    databaseReference.child("image").setValue(it.toString())
                    databaseReference.child("menuid").setValue(menuid.toString())
                    databaseReference.child("name").setValue(name)
                    databaseReference.child("price").setValue(price)

                    Toast.makeText(this, "Add product successfully", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                    menu_image.visibility = View.GONE
                    menu_name.text = null
                    menu_price.text = null
                }
            }
                .addOnFailureListener {
                    println("Info File : ${it.message}")
                }


        }
        else
        {
            Toast.makeText(this,"Please Enter the name of student", Toast.LENGTH_LONG).show()
        }
    }

    private fun LoadData()
    {

        // show progress bar when call method as loading concept
        progressBar.visibility = View.VISIBLE

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError)
            {
                Toast.makeText(applicationContext,"Error Encounter Due to "+databaseError.message, Toast.LENGTH_LONG).show()/**/

            }

            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    //before fetch we have clear the list not to show duplicate value
                    menuList.clear()
                    // fetch data & add to list
                    for (data in dataSnapshot.children)
                    {
                        val std = data.getValue(Menu::class.java)
                        menuList.add(std!!)
                    }

                    // bind data to adapter
                    val adapter =
                        MenuAdapter(menuList, this@MenuActivity)
                    recyclerview.adapter = adapter
                    progressBar.visibility = View.GONE
                    adapter.notifyDataSetChanged()

                }
                else
                {
                    // if no data found or you can check specefici child value exist or not here
                    Toast.makeText(applicationContext,"No data Found", Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE
                }

            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            imgPath = data?.data

            try {
                //getting image from gallery
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imgPath)

                //Setting image to ImageView
                menu_image.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}
