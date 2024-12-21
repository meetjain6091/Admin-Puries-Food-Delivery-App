package com.example.adminpuriesfooddelivery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminpuriesfooddelivery.adapter.DeliveryAdapter
import com.example.adminpuriesfooddelivery.databinding.ActivityOutForDeliveryBinding
import com.example.adminpuriesfooddelivery.model.OrdersModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding: ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }

    private lateinit var database: FirebaseDatabase
    private var listOfCompleteOrderList: ArrayList<OrdersModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            finish()
        }
        retrieveCompleteOrderDetails()


    }

    private fun retrieveCompleteOrderDetails() {
        database = FirebaseDatabase.getInstance()
        val completeOrderRef = database.reference.child("CompleteOrder").orderByChild("currentTime")
        completeOrderRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfCompleteOrderList.clear()
                for (orderSnapshot in snapshot.children) {
                    val completeOrder = orderSnapshot.getValue(OrdersModel::class.java) // Use the correct model class
                    completeOrder?.let {
                        listOfCompleteOrderList.add(it)
                    }
                    listOfCompleteOrderList.reverse()
                    setDataintoRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
    }
    private fun setDataintoRecyclerView(){
        val customername  = mutableListOf<String>()
        val moneyStatus  = mutableListOf<Boolean>()

        for(order in listOfCompleteOrderList){
            order.userName?.let{
                customername.add(it)
            }
            moneyStatus.add(order.paymentReceived)
        }
        val adapter = DeliveryAdapter(customername,moneyStatus) // Ensure this adapter matches your OrdersModel
        binding.deliveryRecyclerView.adapter = adapter
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)
    }
}
