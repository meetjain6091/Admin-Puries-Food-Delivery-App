package com.example.adminpuriesfooddelivery

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminpuriesfooddelivery.adapter.PendingOrderAdapter
import com.example.adminpuriesfooddelivery.databinding.ActivityPendingOrderBinding
import com.example.adminpuriesfooddelivery.model.OrdersModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PendingOrderActivity : AppCompatActivity(), PendingOrderAdapter.OnItemClicked {
    private lateinit var userId: String
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
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        database = FirebaseDatabase.getInstance()
        databaseOrderDetails = database.reference.child("OrderDetails")

        Log.d("PendingOrderActivity", "Fetching order details for userId: $userId")

        getOrderDetails()

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun getOrderDetails() {
        databaseOrderDetails.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("PendingOrderActivity", "onDataChange triggered")
                if (snapshot.exists()) {
                    Log.d("PendingOrderActivity", "Snapshot exists. Children count: ${snapshot.childrenCount}")
                    listOfOrderItem.clear() // Clear the list before adding new data
                    for (orderSnapshot in snapshot.children) {
                        val orderDetails = orderSnapshot.getValue(OrdersModel::class.java)
                        if (orderDetails != null) {
                            // Convert totalprice from Long to String if it's not null
                            orderDetails.totalprice = orderDetails.totalprice?.toString()
                            listOfOrderItem.add(orderDetails)
                            Log.d("PendingOrderActivity", "Order added: $orderDetails")
                        } else {
                            Log.e("PendingOrderActivity", "OrderDetails is null for snapshot: $orderSnapshot")
                        }
                    }
                    // Set the adapter after all data is retrieved
                    addListOfData()
                } else {
                    Log.e("PendingOrderActivity", "Snapshot does not exist.")
                }
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                Log.e("PendingOrderActivity", "Database error: ${error.message}")
                Toast.makeText(
                    this@PendingOrderActivity,
                    "Cancelled: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun addListOfData() {
        Log.d("PendingOrderActivity", "Preparing data for adapter")
        for (orderItem in listOfOrderItem) {
            orderItem.username?.let { listOfName.add(it) }
            orderItem.totalprice?.let { listOfTotalPrice.add(it) }
        }
        Log.d("PendingOrderActivity", "Data prepared: Names - $listOfName, Prices - $listOfTotalPrice")
        setAdapter()
    }

    private fun setAdapter() {
        Log.d("PendingOrderActivity", "Setting adapter with ${listOfOrderItem.size} items")
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
        val userOrderDetails: Parcelable = listOfOrderItem[position] // Cast to Parcelable
        intent.putExtra("UserOrderDetails", userOrderDetails)
        startActivity(intent)
    }

    private fun updateOrderAcceptedStatus(position: Int) {
        val userIdOfClickItem = listOfOrderItem[position].useruid
        val pushKeyOfClickItem = listOfOrderItem[position].itempushkey
        if (userIdOfClickItem != null && pushKeyOfClickItem != null) {
            val reBuyRef =
                database.reference.child("user").child(userIdOfClickItem).child("BuyHistory")
                    .child(pushKeyOfClickItem)

            reBuyRef.setValue(listOfOrderItem[position])

            reBuyRef.child("orderAccepted").setValue(true)
            databaseOrderDetails.child(pushKeyOfClickItem).child("orderAccepted").setValue(true)
        }
    }

    override fun onItemAcceptClickListener(position: Int) {
        val childItemPushKey = listOfOrderItem[position].itempushkey
        val clickOrderReferenec = childItemPushKey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickOrderReferenec?.child("orderAccepted")?.setValue(true)
        updateOrderAcceptedStatus(position)
    }

    override fun onItemDispatchClickListener(position: Int) {
        val dispatchItemPushKey = listOfOrderItem[position].itempushkey!!
        val dispatchItemOrderRef =
            database.reference.child("CompletedOrder").child(dispatchItemPushKey)

        dispatchItemOrderRef.setValue(listOfOrderItem[position])
            .addOnSuccessListener {
                deleteThisItemFromOrders(dispatchItemPushKey)
            }
    }

    private fun deleteThisItemFromOrders(dispatchItemPushKey: String) {
        val orderDetailsRef =
            database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderDetailsRef.removeValue()
            .addOnSuccessListener {
                Log.d("PendingOrderActivity", "Order removed successfully for key: $dispatchItemPushKey")
                Toast.makeText(this, "Order has been accepted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.e("PendingOrderActivity", "Failed to remove order for key: $dispatchItemPushKey")
                Toast.makeText(this, "Failed to accept order", Toast.LENGTH_SHORT).show()
            }
    }
}
