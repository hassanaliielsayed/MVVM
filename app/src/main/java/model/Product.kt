package com.example.courotines.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "products")
data class Product(
    val availabilityStatus: String,
    val brand: String?,
    val category: String,
    val description: String,
    val discountPercentage: Double,
    @PrimaryKey
    val id: Int,
    val minimumOrderQuantity: Int,
    val price: Double,
    val rating: Double,
    val returnPolicy: String,
    val shippingInformation: String,
    val sku: String,
    val stock: Int,
    val thumbnail: String,
    val title: String,
    val warrantyInformation: String,
    val weight: Int
): Serializable
