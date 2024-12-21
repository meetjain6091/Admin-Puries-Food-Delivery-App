package com.example.adminpuriesfooddelivery

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminpuriesfooddelivery.adapter.PendingOrderAdapter
import com.example.adminpuriesfooddelivery.databinding.ActivityPendingOrderBinding
import com.example.adminpuriesfooddelivery.model.OrdersModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrderActivity : AppCompatActivity(), PendingOrderAdapter.OnItemClicked {
    private var listOfName: MutableList<String> = mutableListOf()
    private var listOfTotalPrice: MutableList<String> = mutableListOf()
    private var listOfOrderItem: ArrayList<OrdersModel> = arrayListOf()
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetails: DatabaseReference

    private val binding: ActivityPendingOrderBinding by lazy {
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
        databaseOrderDetails = database.reference.child("OrderDetails")

        getOrderDetails()

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun getOrderDetails() {
        databaseOrderDetails.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the list before adding new data

                for (orderSnapshot in snapshot.children) {

                    for (orderItemSnapshot in snapshot.children) {
                        val orderItem = orderItemSnapshot.getValue(OrdersModel::class.java)
                        orderItem?.let { listOfOrderItem.add(it) }
                    }
                }
                // Set the adapter after all data is retrieved
                addListOfData()
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                Toast.makeText(
                    this@PendingOrderActivity,
                    "Cancelled: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun addListOfData() {
        for (orderItem in listOfOrderItem) {
            orderItem.userName?.let { listOfName.add(it) }
            orderItem.totalPrice?.let { listOfTotalPrice.add(it) }

        }
        setAdapter()
    }

    private fun setAdapter() {
        val adapter = PendingOrderAdapter(
            this,
            listOfName, listOfTotalPrice, this

        )
        binding.pendingOrderRecyclerView.layoutManager =
            LinearLayoutManager(this@PendingOrderActivity)
        binding.pendingOrderRecyclerView.adapter = adapter
    }

    override fun onItemClickListener(position: Int) {
        val intent = Intent(this, OrdersDetailActivity::class.java)
        val userOrderDetails = listOfOrderItem[position] // Get the selected OrdersModel object
        intent.putExtra("UserOrderdetails", userOrderDetails) // Pass the object
        startActivity(intent)
    }

    private fun updateOrderAcceptedStatus(position: Int) {
        val userIdOfClickItem = listOfOrderItem[position].userId
        val pushKeyOfClickItem = listOfOrderItem[position].itemPushKey
        if (userIdOfClickItem != null && pushKeyOfClickItem != null) {
            val reBuyRef =
                database.reference.child("Users").child(userIdOfClickItem!!).child("ButHistory")
                    .child(pushKeyOfClickItem!!)

            reBuyRef.setValue(listOfOrderItem[position])

            reBuyRef.child("orderAccepted").setValue(true)
            databaseOrderDetails.child(pushKeyOfClickItem).child("orderAccepted").setValue(true)

        }
    }

    override fun onItemAcceptClickListener(position: Int) {
        val childItemPushKey = listOfOrderItem[position].itemPushKey
        val clickOrderReferenec = childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickOrderReferenec?.child("orderAccepted")?.setValue(true)
        updateOrderAcceptedStatus(position)
    }

    override fun onItemDispatchClickListener(position: Int) {
        val dispatchItemPushKey = listOfOrderItem[position].itemPushKey!!
        val dispatchItemOrderRef =
            database.reference.child("CompletedOrder").child(dispatchItemPushKey!!)

        dispatchItemOrderRef.setValue(listOfOrderItem[position])
            .addOnSuccessListener {
                deleteThisItemFromOrders(dispatchItemPushKey)
            }


    }

    private fun deleteThisItemFromOrders(dispatchItemPushKey: String) {
        val orderDetailsRef =
            database.reference.child("OrderDetails").child(dispatchItemPushKey!!)
        orderDetailsRef.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Order has been accepted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to accept order", Toast.LENGTH_SHORT).show()
            }
    }

}