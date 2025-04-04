package com.example.assignment_habit.view.components.screens

import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.assignment_habit.ui.theme.DarkRed
import com.example.assignment_habit.ui.theme.PrimaryRed
import com.example.assignment_habit.viewModel.HomeViewModel

import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Notifications


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.zIndex

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val cartCount by viewModel.cartItemCount.collectAsState()
    val notificationCount = 2


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Food Explorer", fontWeight = FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = PrimaryRed),
                navigationIcon = {
                    IconButton(onClick = { /* Location/Address selection */ }) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Change Location",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Search icon
                    IconButton(onClick = { /* Search action */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }

                    // Notifications icon with badge
                    IconWithBadge(
                        icon = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        badgeCount = notificationCount,
                        onClick = { /* Notifications action */ }
                    )

                    // Favorites/Wishlist icon
                    IconButton(onClick = { /* Favorites action */ }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Favorites",
                            tint = Color.White
                        )
                    }

                    // Shopping Cart icon with badge
                    IconWithBadge(
                        icon = Icons.Default.ShoppingCart,
                        contentDescription = "Cart",
                        badgeCount = cartCount,
                        onClick = { /* Shopping cart action */ }
                    )
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryRed)
            }
        } else if (uiState.error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Error: ${uiState.error}")
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                DiscountBanner(modifier = Modifier.padding(16.dp))

                // First food section with real data
                TriedTastedLovedSection(
                    foods = uiState.triedTastedLoved,
                    onAddClick = viewModel::addToCart,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                // Second food section with real data
                LookingForMoreSection(
                    foods = uiState.lookingForMore,
                    onAddClick = viewModel::addToCart,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                MoreFoodBanner(modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun IconWithBadge(
    icon: ImageVector,
    contentDescription: String,
    badgeCount: Int,
    onClick: () -> Unit
) {
    Box(contentAlignment = Alignment.Center) {
        IconButton(onClick = onClick) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = Color.White
            )
        }

        if (badgeCount > 0) {
            // Improved badge positioning
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = (-2).dp, y = 6.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, PrimaryRed, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (badgeCount > 9) "9+" else badgeCount.toString(),
                    color = PrimaryRed,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun DiscountBanner(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                PrimaryRed.copy(alpha = 0.9f),
                                DarkRed.copy(alpha = 0.7f)
                            )
                        )
                    )
            )

            // Safe image implementation
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .fillMaxWidth(0.5f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://images.unsplash.com/photo-1565299624946-b28f40a0ae38")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Pizza deal",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .alpha(0.85f),
                    error = ColorPainter(DarkRed.copy(alpha = 0.3f)),
                    fallback = ColorPainter(DarkRed.copy(alpha = 0.3f))
                )
            }

            // Content
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.6f)
                    .align(Alignment.CenterStart)
            ) {
                Text(
                    text = "50% OFF",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.3f),
                            offset = Offset(2f, 2f),
                            blurRadius = 3f
                        )
                    )
                )

                Text(
                    text = "On your first order",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    ),
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Button(
                    onClick = { /* Order Now action */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.padding(top = 8.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Order Now",
                        color = DarkRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MoreFoodBanner(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color(0xFF000000).copy(alpha = 0.2f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Banner content
            Column(
                modifier = Modifier
                    .weight(0.6f)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Hungry?",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkRed
                    )
                )

                Text(
                    text = "Explore our curated collection of premium dishes",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                TextButton(
                    onClick = { /* View All action */ },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = PrimaryRed
                    )
                ) {
                    Text(
                        text = "View All",
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "View All",
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(0.4f)
                    .fillMaxHeight()
            ) {
                // Overlay gradient for better text visibility
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    Color.White,
                                    Color.White.copy(alpha = 0.0f)
                                ),
                                startX = 0f,
                                endX = 100f
                            )
                        )
                        .zIndex(1f)
                )

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://images.unsplash.com/photo-1504674900247-0877df9cc836")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Delicious Food",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    error = ColorPainter(PrimaryRed.copy(alpha = 0.2f)),
                    fallback = ColorPainter(PrimaryRed.copy(alpha = 0.2f))
                )
            }
        }
    }
}