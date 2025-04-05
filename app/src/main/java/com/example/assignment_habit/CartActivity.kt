
package com.example.assignment_habit

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.assignment_habit.Data.model.FoodItem
import com.example.assignment_habit.ui.theme.Assignment_HabitTheme
import com.example.assignment_habit.ui.theme.DarkRed
import com.example.assignment_habit.ui.theme.PrimaryRed
import kotlin.text.get

class CartActivity : ComponentActivity() {
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cartItems = intent.getSerializableExtra("cartItems") as? ArrayList<FoodItem> ?: arrayListOf()

        setContent {
            Assignment_HabitTheme {
                CartScreen(
                    cartItems = cartItems,
                    onBackClick = { finish() },
                    onCheckout = {
                        Toast.makeText(this, "Proceeding to checkout: ₹${String.format("%.2f", it)}", Toast.LENGTH_SHORT).show()
                        // TODO("Implement checkout logic")
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    cartItems: List<FoodItem>,
    onBackClick: () -> Unit,
    onCheckout: (Double) -> Unit
) {
    // Group identical items by ID
    val groupedItems = remember {
        cartItems.groupBy { it.id }
            .mapValues { (_, items) -> Pair(items.first(), items.size) }
    }

    // Create a consolidated list with only unique items
    val consolidatedItems = remember {
        mutableStateListOf<FoodItem>().apply {
            addAll(groupedItems.values.map { it.first })
        }
    }

    // Initialize quantities map with the counts from grouped items
    val itemQuantities = remember {
        mutableStateMapOf<String, Int>().apply {
            groupedItems.forEach { (id, pair) ->
                this[id] = pair.second
            }
        }
    }

    // Calculate total based on price and quantity
    val totalAmount = consolidatedItems.sumOf {
        it.price * (itemQuantities[it.id] ?: 1)
    }

    // Callbacks for cart management
    val onRemoveItem: (FoodItem) -> Unit = { item ->
        consolidatedItems.remove(item)
        itemQuantities.remove(item.id)
    }

    val onQuantityChange: (FoodItem, Int) -> Unit = { item, newQuantity ->
        itemQuantities[item.id] = newQuantity
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Your Cart",
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = DarkRed
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = DarkRed
                ),
                modifier = Modifier.shadow(
                    elevation = 4.dp,
                    spotColor = Color.Black.copy(alpha = 0.1f)
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F8F8))
        ) {
            if (consolidatedItems.isEmpty()) {
                EmptyCartView(onBrowseMenuClick = onBackClick)
            }else {
                Column(modifier = Modifier.weight(1f)) {
                    // Cart summary card
                    CartSummaryCard(
                        itemCount = itemQuantities.values.sum(),
                        totalAmount = totalAmount,
                        modifier = Modifier
                            .padding(16.dp)
                            .animateContentSize()
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
                    ) {
                        items(consolidatedItems) { item ->
                            CartItemCard(
                                item = item,
                                quantity = itemQuantities[item.id] ?: 1,
                                onRemoveItem = onRemoveItem,
                                onQuantityChange = onQuantityChange
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }

                CheckoutButton(
                    totalAmount = totalAmount,
                    onCheckoutClick = { onCheckout(totalAmount) }
                )
            }
        }
    }
}

@Composable
fun EmptyCartView(onBrowseMenuClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .background(PrimaryRed.copy(alpha = 0.08f), RoundedCornerShape(120.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://images.unsplash.com/photo-1586769852044-692d6e3703f2")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Empty cart",
                    modifier = Modifier
                        .size(180.dp)
                        .alpha(0.8f),
                    error = ColorPainter(Color.Gray.copy(alpha = 0.3f))
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Your cart is empty",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold,
                color = DarkRed
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Add some delicious food items to your cart",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onBrowseMenuClick,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .height(50.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(12.dp),
                        spotColor = Color.Black.copy(alpha = 0.2f)
                    )
            ) {
                Text(
                    text = "Browse Menu",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun CartItemCard(
    item: FoodItem,
    quantity: Int,
    onRemoveItem: (FoodItem) -> Unit,
    onQuantityChange: (FoodItem, Int) -> Unit
) {
    val showDeleteDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image with gradient overlay
            Box(
                modifier = Modifier
                    .weight(0.35f)
                    .height(130.dp)
            ) {
                // Gradient overlay for better text visibility
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.White
                                ),
                                startX = Float.POSITIVE_INFINITY,
                                endX = 0f
                            )
                        )
                        .zIndex(1f)
                )

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = item.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    error = ColorPainter(PrimaryRed.copy(alpha = 0.2f))
                )
            }

            // Item details
            Column(
                modifier = Modifier
                    .weight(0.65f)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(
                        onClick = { showDeleteDialog.value = true },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Remove",
                            tint = PrimaryRed.copy(alpha = 0.7f)
                        )
                    }
                }

                Text(
                    text = "${item.description.take(40)}${if (item.description.length > 40) "..." else ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${String.format("%.2f", item.price)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkRed
                    )

                    EnhancedQuantitySelector(
                        initialValue = quantity,
                        onQuantityChange = { newQuantity ->
                            onQuantityChange(item, newQuantity)
                        }
                    )
                }
            }
        }
    }

    if (showDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog.value = false },
            title = {
                Text(
                    "Remove Item",
                    fontWeight = FontWeight.Bold,
                    color = DarkRed
                )
            },
            text = {
                Text("Are you sure you want to remove ${item.name} from your cart?")
            },
            confirmButton = {
                TextButton(onClick = {
                    onRemoveItem(item)
                    Toast.makeText(context, "${item.name} removed from cart", Toast.LENGTH_SHORT).show()
                    showDeleteDialog.value = false
                }) {
                    Text(
                        "Remove",
                        color = PrimaryRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog.value = false }) {
                    Text(
                        "Cancel",
                        fontWeight = FontWeight.Medium
                    )
                }
            },
            containerColor = Color.White,
            tonalElevation = 4.dp
        )
    }
}

@Composable
fun EnhancedQuantitySelector(
    initialValue: Int = 1,
    onQuantityChange: (Int) -> Unit = {}
) {
    var quantity by remember { mutableStateOf(initialValue) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(28.dp)
                .background(
                    if (quantity > 1) PrimaryRed else Color.LightGray,
                    RoundedCornerShape(6.dp)
                )
                .clickable(enabled = quantity > 1) {
                    if (quantity > 1) {
                        quantity--
                        onQuantityChange(quantity)
                    }
                }
        ) {
            Text(
                text = "-",
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }

        Text(
            text = "$quantity",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(28.dp)
                .background(PrimaryRed, RoundedCornerShape(6.dp))
                .clickable {
                    quantity++
                    onQuantityChange(quantity)
                }
        ) {
            Text(
                text = "+",
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }
    }
}

@Composable
fun CheckoutButton(
    totalAmount: Double,
    onCheckoutClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.25f)
            )
            .background(Color.White, RoundedCornerShape(16.dp))
    ) {
        Button(
            onClick = onCheckoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = DarkRed,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Proceed to Checkout",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "₹${String.format("%.2f", totalAmount)}",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun CartSummaryCard(itemCount: Int, totalAmount: Double, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color.Black.copy(alpha = 0.15f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Cart Summary",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = "$itemCount item${if (itemCount > 1) "s" else ""}",
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }

            Divider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = Color.LightGray.copy(alpha = 0.7f),
                thickness = 1.dp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Amount",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "₹${String.format("%.2f", totalAmount)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkRed
                )
            }
        }
    }
}
