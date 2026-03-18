package com.example.adminpuriesfooddelivery.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class OrdersModel(
    var useruid: String? = null,
    var username: String? = null,
    var foodnames: MutableList<String>? = null,
    var foodprices: MutableList<String>? = null,
    var foodquantitys: MutableList<String>? = null,
    var totalprice: String? = null,
    var address: String? = null,
    var phonenumber: String? = null,
    var orderisAccepted: Boolean = false,
    var paymentisReceived: Boolean = false,
    var itempushkey: String? = null,
) : Serializable, Parcelable {

    constructor(parcel: Parcel) : this(
        useruid = parcel.readString(),
        username = parcel.readString(),
        foodnames = parcel.createStringArrayList()?.toMutableList(),
        foodprices = parcel.createStringArrayList()?.toMutableList(),
        foodquantitys = parcel.createStringArrayList()?.toMutableList(),
        totalprice = parcel.readString(),
        address = parcel.readString(),
        phonenumber = parcel.readString(),
        orderisAccepted = parcel.readByte() != 0.toByte(),
        paymentisReceived = parcel.readByte() != 0.toByte(),
        itempushkey = parcel.readString(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(useruid)
        parcel.writeString(username) // Corrected the duplicate line
        parcel.writeStringList(foodnames)
        parcel.writeStringList(foodprices)
        parcel.writeStringList(foodquantitys)
        parcel.writeString(totalprice)
        parcel.writeString(address)
        parcel.writeString(phonenumber)
        parcel.writeByte(if (orderisAccepted) 1 else 0)
        parcel.writeByte(if (paymentisReceived) 1 else 0)
        parcel.writeString(itempushkey) // Fixed for itemPushKey
        // Corrected the write value for currentTime
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<OrdersModel> {
        override fun createFromParcel(parcel: Parcel): OrdersModel = OrdersModel(parcel)
        override fun newArray(size: Int): Array<OrdersModel?> = arrayOfNulls(size)
    }
}
