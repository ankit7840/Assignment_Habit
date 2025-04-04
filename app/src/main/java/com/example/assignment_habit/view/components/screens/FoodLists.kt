package com.example.assignment_habit.view.components.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


import androidx.compose.ui.unit.sp
import com.example.assignment_habit.ui.theme.DarkRed
import com.example.assignment_habit.ui.theme.PrimaryRed

// ui/components/FoodLists.kt
@Composable
fun TriedTastedLovedSection(
    foods: List<FoodItem>,
    onAddClick: (FoodItem) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Section Title
        Text(
            text = "Tried, Tasted and Loved",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = DarkRed
            ),
            letterSpacing = 0.8.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            PrimaryRed.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(start = 12.dp, top = 8.dp, bottom = 8.dp)
        )

        // Safely handle empty list case
        if (foods.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No items available")
            }
        } else {
            // Safe implementation with fixed height items
            LazyRow(
                contentPadding = PaddingValues(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(foods) { food ->
                    // Using SafeFoodCard
                    FoodCard(
                        food = food,
                        onAddClick = { onAddClick(food) },
                        isHorizontal = true
                    )
                }
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
        // Section Title
        Text(
            text = "Looking for even more",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 24.sp,
                color = DarkRed
            ),
            letterSpacing = 0.8.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            PrimaryRed.copy(alpha = 0.1f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(start = 12.dp, top = 8.dp, bottom = 8.dp)
        )

        // Safely handle empty list case
        if (foods.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No items available")
            }
        } else {
            // Use LazyColumn with fixed height boundaries
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    // Set a maximum height to prevent unbounded measurement issues
                    .height((foods.size * 240).coerceAtMost(720).dp)
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
}