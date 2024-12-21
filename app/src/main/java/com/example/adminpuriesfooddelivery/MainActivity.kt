package com.example.adminpuriesfooddelivery

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminpuriesfooddelivery.databinding.ActivityAddItemBinding
import com.example.adminpuriesfooddelivery.databinding.ActivityMainBinding
import com.example.adminpuriesfooddelivery.model.OrdersModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var  database : FirebaseDatabase
    private lateinit var auth : FirebaseAuth
    private lateinit var completedOrderref : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.addMenu.setOnClickListener{
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
        binding.allItemMenu.setOnClickListener{
            val intent = Intent(this, AllItemActivity::class.java)
            startActivity(intent)
        }
        binding.outForDeliveryButton.setOnClickListener{
            val intent = Intent(this, OutForDeliveryActivity::class.java)
            startActivity(intent)
        }
        binding.profileButton.setOnClickListener{
            val intent = Intent(this, AdminProfileActivity::class.java)
            startActivity(intent)
        }
        binding.createUser.setOnClickListener{
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }
        binding.pendingOrderTextView.setOnClickListener{
            val intent = Intent(this, PendingOrderActivity::class.java)
            startActivity(intent)
        }

        binding.logoutBtn.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        pendingOrders()
        completedOrders()
        wholeTimeEarning()

    }
    private fun pendingOrders(){
        database = FirebaseDatabase.getInstance()
        var pendingOrderRef = database.reference.child("OrderDetails")
        var pendingOrderitemCount = 0
        pendingOrderRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderitemCount = snapshot.childrenCount.toInt()
                binding.pendingNoOfOrders.text = pendingOrderitemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    private fun completedOrders(){
        var pendingOrderRef = database.reference.child("CompletedOrder")
        var pendingOrderitemCount = 0
        pendingOrderRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                pendingOrderitemCount = snapshot.childrenCount.toInt()
                binding.completedOrders.text = pendingOrderitemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun wholeTimeEarning(){
        var listOfTotalPay = mutableListOf<Int>()
        completedOrderref = FirebaseDatabase.getInstance().reference.child("CompletedOrder")
        completedOrderref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(order in snapshot.children){
                    var completeOrder = order.getValue(OrdersModel::class.java)
                    completeOrder?.totalPrice?.replace("₹","")?.toIntOrNull()
                        ?.let {
                            i->
                            listOfTotalPay.add(i)
                        }
                }
                binding.wholeTimeEarning.text = listOfTotalPay.sum().toString()+"₹"
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}