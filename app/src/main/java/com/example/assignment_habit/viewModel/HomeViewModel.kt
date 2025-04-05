package com.example.assignment_habit.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment_habit.Data.model.FoodItem
import com.example.assignment_habit.Data.repository.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ui/viewmodel/HomeViewModel.kt
class HomeViewModel(private val repository: FoodRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _cartItemCount = MutableStateFlow(0)
    val cartItemCount: StateFlow<Int> = _cartItemCount.asStateFlow()

    private val _cartItems = MutableStateFlow<List<FoodItem>>(emptyList())
    val cartItems: StateFlow<List<FoodItem>> = _cartItems.asStateFlow()

    init {
        loadFoodData()
    }

    private fun loadFoodData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getFoodData()
                .onSuccess { foodData ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            triedTastedLoved = foodData.triedTastedLoved,
                            lookingForMore = foodData.lookingForMore
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    fun addToCart(item: FoodItem) {
        _cartItems.update { currentItems -> currentItems + item }
        _cartItemCount.update { it + 1 }
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val triedTastedLoved: List<FoodItem> = emptyList(),
    val lookingForMore: List<FoodItem> = emptyList(),
    val error: String? = null
)