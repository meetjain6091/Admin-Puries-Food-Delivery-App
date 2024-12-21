package com.example.adminpuriesfooddelivery.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminpuriesfooddelivery.databinding.OrderDetailsItemBinding

class OrdersDetailAdapter(
    private val context: Context,
    private val foodName : ArrayList<String>,
    private val foodQuantity: ArrayList<String>,
    private val foodPrice: ArrayList<String>

) : RecyclerView.Adapter<OrdersDetailAdapter.OrderDetailsViewHolder>() {
    class OrderDetailsViewHolder(private val binding: OrderDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            foodName: MutableList<String>,
            foodQuantity: MutableList<String>,
            foodPrice: MutableList<String>,
            position: Int,
            context: Context
        ) {
            binding.apply {
                foodNameTV.text = foodName[position]
                foodQuantityTV.text = foodQuantity[position]
                foodPriceTV.text = foodPrice[position]
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderDetailsViewHolder {
        val binding =
            OrderDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: OrderDetailsViewHolder,
        position: Int
    ) {
        holder.bind(foodName, foodQuantity, foodPrice, position, context)
    }

    override fun getItemCount(): Int = foodName.size

}