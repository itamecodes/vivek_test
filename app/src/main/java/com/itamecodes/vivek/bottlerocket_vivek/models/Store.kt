package com.itamecodes.vivek.bottlerocket_vivek.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "store_table",indices = arrayOf(
    Index(value = ["name", "address"],
    unique = true)
))
data class Store(
    @PrimaryKey(autoGenerate = true)
    val id: Long?,
    @ColumnInfo(name = "address")
    val address: String?,
    @ColumnInfo(name = "city")
    val city: String?,
    @ColumnInfo(name = "name")
    val name: String?,
    @ColumnInfo(name = "latitude")
    val latitude: String?,
    @ColumnInfo(name = "zipcode")
    val zipcode: String?,
    @ColumnInfo(name = "storeLogoURL")
    val storeLogoURL: String?,
    @ColumnInfo(name = "phone")
    val phone: String?,
    @ColumnInfo(name = "longitude")
    val longitude: String?,
    @ColumnInfo(name = "storeID")
    val storeID: String?,
    @ColumnInfo(name = "state")
    val state: String?
){
    val completeAddress:String?
        get()= listOfNotNull(address,city,state,zipcode).joinToString(", ")

}
