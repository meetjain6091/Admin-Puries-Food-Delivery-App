package com.example.adminpuriesfooddelivery

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminpuriesfooddelivery.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminpuriesfooddelivery.databinding.ActivityPendingOrderBinding
import com.example.adminwavesoffood.adapter.PendingOrderAdapter

class PendingOrderActivity : AppCompatActivity() {
    private val binding: ActivityPendingOrderBinding by lazy {
        ActivityPendingOrderBinding .inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            finish()
        }
        val orderedCustomerNames= arrayListOf("John Doe","Jane Smith","Mike Johnson")
        val orderedQuantity= arrayListOf("8","7","9")
        val orderedFoodImage= arrayListOf(R.drawable.menu1,R.drawable.menu2,R.drawable.menu3)
        val adapter= PendingOrderAdapter(orderedCustomerNames,orderedQuantity,orderedFoodImage,this)
        binding.pendingOrderRecyclerView.adapter=adapter
        binding.pendingOrderRecyclerView.layoutManager= LinearLayoutManager(this)
    }
}