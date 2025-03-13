package com.plcoding.cameraxguide.ui.view

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.plcoding.cameraxguide.MainActivity.Companion.CAMERAX_PERMISSIONS

// Define bottom nav items with routes, icons and labels.
sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    object Tab1 : BottomNavItem("tab1", Icons.Default.Home, "Tab 1")
    object Tab2 : BottomNavItem("tab2", Icons.Default.Favorite, "Tab 2")
    object Tab3 : BottomNavItem("tab3", Icons.Default.Settings, "Tab 3")
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        // The NavHost with nested graphs for each tab.
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Tab1.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Tab 1 nested navigation graph
            navigation(
                startDestination = "tab1_home",
                route = BottomNavItem.Tab1.route
            ) {
                composable("tab1_home") { Tab1HomeScreen(navController) }
                composable("tab1_detail") { Tab1DetailScreen(navController) }
            }
            // Tab 2 nested navigation graph
            navigation(
                startDestination = "tab2_home",
                route = BottomNavItem.Tab2.route
            ) {
                composable("tab2_home") { Tab2HomeScreen(navController) }
                composable("tab2_detail") { Tab2DetailScreen(navController) }
            }
            // Tab 3 nested navigation graph
            navigation(
                startDestination = "tab3_home",
                route = BottomNavItem.Tab3.route
            ) {
                composable("tab3_home") { Tab3HomeScreen(navController) }
                composable("tab3_detail") { Tab3DetailScreen(navController) }
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(BottomNavItem.Tab1, BottomNavItem.Tab2, BottomNavItem.Tab3)
    BottomNavigation {
        // Observe the current back stack to mark the selected tab.
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        // Here we check if the current route contains our tab route.
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute?.contains(item.route) == true,
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination to avoid stacking
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

/* ----- Sample Screen Composables ----- */
@Composable
fun Tab1HomeScreen(navController: NavController) {
    val context = LocalContext.current

    // Your UI for Tab 1 home screen
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tab 1 Home")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("tab1_detail") }) {
            Text("Go to Detail")
        }

        CameraScreen(context) { hasRequiredPermissions(context) }
    }
}

fun hasRequiredPermissions(context: Context): Boolean {
    return CAMERAX_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            context,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
}

@Composable
fun Tab1DetailScreen(navController: NavController) {
    // Your UI for Tab 1 detail screen
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Tab 1 Detail")
    }
}

@Composable
fun Tab2HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tab 2 Home")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("tab2_detail") }) {
            Text("Go to Detail")
        }
    }
}

@Composable
fun Tab2DetailScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Tab 2 Detail")
    }
}

@Composable
fun Tab3HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tab 3 Home")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("tab3_detail") }) {
            Text("Go to Detail")
        }
    }
}

@Composable
fun Tab3DetailScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Tab 3 Detail")
    }
}