package com.example.adminpuriesfooddelivery


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminpuriesfooddelivery.adapter.MenuItemAdapter
import com.example.adminpuriesfooddelivery.model.AllMenu
import com.example.adminpuriesfooddelivery.databinding.ActivityAllItemBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllItemActivity : AppCompatActivity() {
    private lateinit var databaseReference : DatabaseReference
    private lateinit var database : FirebaseDatabase
    private  var menuItems : ArrayList<AllMenu> = ArrayList()
    private val binding: ActivityAllItemBinding by lazy {
        ActivityAllItemBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        databaseReference = FirebaseDatabase.getInstance().reference
        retriveMenuItem()

        binding.backButton.setOnClickListener {
            finish()
        }


    }
    private  fun retriveMenuItem(){
        database = FirebaseDatabase.getInstance()
        val foodRef : DatabaseReference = database.reference.child("menu")
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear data before
                menuItems.clear()
                for(foodSnapShot in snapshot.children){
                    val menuItem = foodSnapShot.getValue(AllMenu::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private fun setAdapter(){
        val adapter= MenuItemAdapter(this@AllItemActivity,menuItems,databaseReference)
        binding.menuRecyclerView.layoutManager= LinearLayoutManager(this)
        binding.menuRecyclerView.adapter=adapter
    }
}