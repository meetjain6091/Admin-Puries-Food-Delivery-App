package com.example.adminpuriesfooddelivery.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class OrdersModel(
    var userId: String? = null,
    var userName: String? = null,
    var foodNames: MutableList<String>? = null,
    var foodPrices: MutableList<String>? = null,
    var foodQuantities: MutableList<String>? = null,
    var totalPrice: String? = null,
    var address: String? = null,
    var phone: String? = null,
    var orderAccepted: Boolean = false,
    var paymentReceived: Boolean = false,
    var itemPushKey: String? = null,
    var currentTime: Long? = null
) :  Serializable {

    constructor(parcel: Parcel) : this(
        userId = parcel.readString(),
        userName = parcel.readString(),
        foodNames = parcel.createStringArrayList()?.toMutableList(),
        foodPrices = parcel.createStringArrayList()?.toMutableList(),
        foodQuantities = parcel.createStringArrayList()?.toMutableList(),
        totalPrice = parcel.readString(),
        address = parcel.readString(),
        phone = parcel.readString(),
        orderAccepted = parcel.readByte() != 0.toByte(),
        paymentReceived = parcel.readByte() != 0.toByte(),
        itemPushKey = parcel.readString(),
        currentTime = parcel.readValue(Long::class.java.classLoader) as? Long
    )

     fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeString(userName)
        parcel.writeStringList(foodNames)
        parcel.writeStringList(foodPrices)
        parcel.writeStringList(foodQuantities)
        parcel.writeString(totalPrice)
        parcel.writeString(address)
        parcel.writeString(phone)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeValue(currentTime)
    }

     fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<OrdersModel> {
        override fun createFromParcel(parcel: Parcel): OrdersModel = OrdersModel(parcel)
        override fun newArray(size: Int): Array<OrdersModel?> = arrayOfNulls(size)
    }
}
