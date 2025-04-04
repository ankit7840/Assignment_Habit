package com.example.assignment_habit.Data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FoodData(
    val triedTastedLoved: List<FoodItem>,
    val lookingForMore: List<FoodItem>
)