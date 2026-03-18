package com.example.adminpuriesfooddelivery

import android.os.Bundle
import android.util.Log
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

        Log.d("OutForDeliveryActivity", "Activity created. Initializing components.")

        binding.backButton.setOnClickListener {
            Log.d("OutForDeliveryActivity", "Back button clicked. Finishing activity.")
            finish()
        }

        retrieveCompleteOrderDetails()
    }

    private fun retrieveCompleteOrderDetails() {
        Log.d("OutForDeliveryActivity", "Starting to retrieve complete order details.")

        database = FirebaseDatabase.getInstance()
        val completeOrderRef = database.reference.child("CompleteOrder").orderByChild("currentTime")

        completeOrderRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("OutForDeliveryActivity", "onDataChange triggered. Snapshot exists: ${snapshot.exists()}. Children count: ${snapshot.childrenCount}")

                listOfCompleteOrderList.clear()

                for (orderSnapshot in snapshot.children) {
                    val completeOrder = orderSnapshot.getValue(OrdersModel::class.java)

                    if (completeOrder != null) {
                        listOfCompleteOrderList.add(completeOrder)
                        Log.d("OutForDeliveryActivity", "Order added: $completeOrder")
                    } else {
                        Log.e("OutForDeliveryActivity", "CompleteOrder is null for snapshot: $orderSnapshot")
                    }
                }

                listOfCompleteOrderList.reverse()
                Log.d("OutForDeliveryActivity", "Order list after reverse: $listOfCompleteOrderList")

                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("OutForDeliveryActivity", "Error retrieving data: ${error.message}")
            }
        })
    }

    private fun setDataIntoRecyclerView() {
        Log.d("OutForDeliveryActivity", "Setting data into RecyclerView.")

        val customerName = mutableListOf<String>()
        val moneyStatus = mutableListOf<Boolean>()

        for (order in listOfCompleteOrderList) {
            order.username?.let {
                customerName.add(it)
                Log.d("OutForDeliveryActivity", "Customer name added: $it")
            }
            moneyStatus.add(order.paymentisReceived)
            Log.d("OutForDeliveryActivity", "Money status added: ${order.paymentisReceived}")
        }

        Log.d("OutForDeliveryActivity", "Final customer names: $customerName")
        Log.d("OutForDeliveryActivity", "Final money statuses: $moneyStatus")

        val adapter = DeliveryAdapter(customerName, moneyStatus) // Ensure this adapter matches your OrdersModel
        binding.deliveryRecyclerView.adapter = adapter
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)

        Log.d("OutForDeliveryActivity", "RecyclerView setup complete.")
    }
}
