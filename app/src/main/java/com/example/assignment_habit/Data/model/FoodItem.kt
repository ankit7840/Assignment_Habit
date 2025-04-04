package com.example.assignment_habit.Data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FoodItem(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val image: String
)
