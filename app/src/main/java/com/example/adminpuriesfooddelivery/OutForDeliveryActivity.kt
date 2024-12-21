package com.example.adminpuriesfooddelivery

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminpuriesfooddelivery.adapter.DeliveryAdapter
import com.example.adminpuriesfooddelivery.databinding.ActivityOutForDeliveryBinding
import com.google.firebase.database.FirebaseDatabase

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }

    private lateinit var database : FirebaseDatabase
    private var listOfCompleteOrderList : ArrayList<OutForDeliveryActivity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            finish()
        }
        retriveCompleteOrderDetails()



        val adapter= DeliveryAdapter()
        binding.deliveryRecyclerView.adapter=adapter
        binding.deliveryRecyclerView.layoutManager= LinearLayoutManager(this)
    }
    private fun retriveCompleteOrderDetails(){
        database = FirebaseDatabase.getInstance()
        val completeOrderRef = database.reference.child("CompleteOrder").orderByChild("currentItem")



    }
}