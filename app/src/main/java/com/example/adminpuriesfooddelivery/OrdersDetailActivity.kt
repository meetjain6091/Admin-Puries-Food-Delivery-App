package com.example.adminpuriesfooddelivery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminpuriesfooddelivery.adapter.OrdersDetailAdapter
import com.example.adminpuriesfooddelivery.databinding.ActivityOrdersDetailBinding
import com.example.adminpuriesfooddelivery.model.OrdersModel

class OrdersDetailActivity : AppCompatActivity() {

    private val binding: ActivityOrdersDetailBinding by lazy {
        ActivityOrdersDetailBinding.inflate(layoutInflater)
    }

    private var userName: String?= null
    private var userAddress: String?= null
    private var userPhone: String?= null
    private var totalPrice: String?= null
    private var foodName: ArrayList<String> = arrayListOf()
    private var foodPrice: ArrayList<String> = arrayListOf()
    private var foodQuantity: ArrayList<String> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        val receivedOrderdetails =
        intent.getSerializableExtra("UserOrderdetails") as OrdersModel
        receivedOrderdetails?.let { orderDetails->

                userName = receivedOrderdetails.userName
                foodName = receivedOrderdetails.foodNames as ArrayList<String>
                foodQuantity = receivedOrderdetails.foodQuantities as ArrayList<String>
                foodPrice = receivedOrderdetails.foodPrices as ArrayList<String>
                userAddress = receivedOrderdetails.address
                userPhone = receivedOrderdetails.phone
                totalPrice = receivedOrderdetails.totalPrice

                setOrderDetailsData()
                setAdapter()

        }?: run {
            // Handle the null case (e.g., show an error message)
            finish() // Exit activity if data is null
        }


    }

    private fun setAdapter() {
        val orderDetailsAdapter =
            OrdersDetailAdapter(this,foodName, foodQuantity, foodPrice)
        binding.orderDetailsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.orderDetailsRecyclerView.adapter = orderDetailsAdapter
    }

    private fun setOrderDetailsData() {
        binding.nameEditText.text = userName
        binding.addressEditText.text = userAddress
        binding.phoneEditText.text = userPhone
        binding.totalPriceText.text = "$totalPrice₹"
    }
}