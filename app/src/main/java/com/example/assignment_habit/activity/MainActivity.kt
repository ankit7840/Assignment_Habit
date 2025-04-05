package com.example.assignment_habit.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.assignment_habit.Data.repository.FoodRepository
import com.example.assignment_habit.ui.theme.Assignment_HabitTheme
import com.example.assignment_habit.view.components.screens.HomeScreen
import com.example.assignment_habit.viewModel.HomeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        val repository = FoodRepository(applicationContext)
        val viewModel = HomeViewModel(repository)

        setContent {
            Assignment_HabitTheme {
                val cartItems = viewModel.cartItems.collectAsState().value

                HomeScreen(viewModel = viewModel,
                    onCartIconClick = {
                        val intent = Intent(this, CartActivity::class.java)
                        intent.putExtra("cartItems", ArrayList(cartItems))
                        startActivity(intent)
                    }
                    )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Assignment_HabitTheme {
        Greeting("Android")
    }
}