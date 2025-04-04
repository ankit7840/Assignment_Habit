package com.example.assignment_habit.Data.repository


import android.content.Context
import com.example.assignment_habit.Data.model.FoodData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class FoodRepository(private val context: Context) {
    suspend fun getFoodData(): Result<FoodData> {
        return try {
            val jsonString = context.assets.open("food_data.json").bufferedReader().use { it.readText() }
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
            val adapter = moshi.adapter(FoodData::class.java)
            val foodData = adapter.fromJson(jsonString) ?: FoodData(emptyList(), emptyList())
            Result.success(foodData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}