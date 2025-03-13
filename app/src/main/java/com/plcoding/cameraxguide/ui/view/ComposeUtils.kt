package com.plcoding.cameraxguide.ui.view

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.materialcore.toRadians
import java.lang.Math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@SuppressLint("RestrictedApi")
@Composable
fun CircularBottomNavigationBar(
    navController: NavController,
    items: List<BottomNavItem>,
    selectedItemIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(Color.White)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Draw the outer circle
            drawCircle(
                color = Color.Gray,
                radius = size.minDimension / 2,
                center = center
            )

            // Draw the navigation items
            items.forEachIndexed { index, item ->
                val angle = (index * 360 / items.size).toFloat()
                val radius = size.minDimension / 2 - 20.toFloat()
                val x = center.x + radius * cos(angle.toRadians())
                val y = center.y + radius * sin(angle.toRadians())

                // Draw the item circle
                drawCircle(
                    color = if (index == selectedItemIndex) Color.Blue else Color.Gray,
                    radius = 20.toFloat(),
                    center = Offset(x, y)
                )

                // Add a touch listener to handle swipes
                val touchHandler = Modifier.pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        // Calculate the angle of the drag
                        val angle = atan2(dragAmount.y, dragAmount.x)

                        // Determine the closest item to the drag angle
                        val closestItemIndex = items.indices.minByOrNull {
                            abs(angle - (it * 360 / items.size).toFloat().toRadians())
                        } ?: selectedItemIndex

                        // Update the selected item
                        onItemSelected(closestItemIndex)
                    }
                }

//                // Add the item icon or text
//                Box(
//                    modifier = touchHandler
//                        .size(40.dp)
//                        .clip(CircleShape)
//                        .background(Color.White)
//                        .clickable { onItemSelected(index) }
//                ) {
//                    Icon(
//                        imageVector = item.icon,
//                        contentDescription = item.label,
//                        tint = Color.Black
//                    )
//                }
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Home Screen")
    }
}

@Composable
fun SearchScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Search Screen")
    }
}

@Composable
fun ProfileScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Profile Screen")
    }
}
