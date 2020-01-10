package com.tugasbesarkotlin4.coffeedelivery.Admin.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.tugasbesarkotlin4.coffeedelivery.Admin.Model.Menu
import com.tugasbesarkotlin4.coffeedelivery.R

class MenuAdapter(val menuList: List<Menu>, val context: Context) : RecyclerView.Adapter<MenuAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_layout, parent, false)
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
        holder.edit.setOnClickListener()
        {
            val perItemPosition = menuList[position]
            updateDialog(perItemPosition)
        }

        // if user click on delete icon for delete operation
        holder.delete.setOnClickListener()
        {
            val perItemPosition = menuList[position]
            deletedata(perItemPosition.menuid)

        }

    }


    // holder class
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById(R.id.textView1) as TextView
        val price = itemView.findViewById(R.id.textView2) as TextView
        val image = itemView.findViewById(R.id.img_product) as ImageView

        // action operation widget
        val edit = itemView.findViewById(R.id.editimage) as ImageView
        val delete = itemView.findViewById(R.id.deleteimage) as ImageView

    }

    // update dialog show method
    private fun updateDialog(perItemPosition: Menu) {

        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.update_dialog, null)
        builder.setCancelable(false)

        val editext1 = view.findViewById<EditText>(R.id.editText1)
        val editext2 = view.findViewById<EditText>(R.id.updatespinerstring)

        // set exist data from recycler to dialog field
        editext1.setText(perItemPosition.name)
        editext2.setText(perItemPosition.price)

        // now set view to builder
        builder.setView(view)
        // now set positive negative button in alertdialog
        builder.setPositiveButton("Update", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

                // update operation below
                val databaseref = FirebaseDatabase.getInstance().getReference("Menu")

                val name = editext1.text.toString()
                val price = editext2.text.toString()
                val image = perItemPosition.image

                if (name.isEmpty() && price.isEmpty())
                {
                    editext1.error = "please Fill up data"
                    editext1.requestFocus()
                    return
                }
                else
                {
                    // update data
                    val std_data = Menu(
                        perItemPosition.menuid,
                        name,
                        price,
                        image
                    )
                    databaseref.child(perItemPosition.menuid).setValue(std_data)
                    Toast.makeText(context, "Data Updated", Toast.LENGTH_SHORT).show()

                }


            }
        })

        builder.setNegativeButton("No", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {


            }
        })
        // show dialog now
        val alert = builder.create()
        alert.show()
    }

    // delete operation
    private fun deletedata(menuid: String)
    {
        val databaseref = FirebaseDatabase.getInstance().getReference("Menu").child(menuid)
        databaseref.removeValue().addOnCompleteListener()
        {
            Toast.makeText(context, "Data Deleted Successfully", Toast.LENGTH_SHORT).show()
        }

    }


}