package com.example.assignment_habit.view.components.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.assignment_habit.Data.model.FoodItem

import androidx.compose.foundation.lazy.items

// ui/components/FoodLists.kt
@Composable
fun TriedTastedLovedSection(
    foods: List<FoodItem>,
    onAddClick: (FoodItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Tried, Tasted and Loved",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(foods) { food ->
                FoodCard(
                    food = food,
                    onAddClick = { onAddClick(food) },
                    isHorizontal = true
                )
            }
        }
    }
}

@Composable
fun LookingForMoreSection(
    foods: List<FoodItem>,
    onAddClick: (FoodItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Looking for even more",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            items(foods) { food ->
                FoodCard(
                    food = food,
                    onAddClick = { onAddClick(food) },
                    isHorizontal = false
                )
            }
        }
    }
}