package com.aylar.reordercompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aylar.reordercompose.ui.reorder.ReorderGrid
import com.aylar.reordercompose.ui.reorder.ReorderImageList
import com.aylar.reordercompose.ui.reorder.ReorderList
import com.aylar.reordercompose.ui.theme.ReorderComposeTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReorderComposeTheme {
                val navController = rememberNavController()
                val navigationItems = listOf(
                    NavigationItem.Lists,
                    NavigationItem.Grids,
                    NavigationItem.Fixed,
                )
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text(stringResource(R.string.app_name)) })
                    },
                    bottomBar = {
                        BottomNavigationBar(navigationItems, navController)
                    }
                ) {
                    Navigation(
                        navController,
                        Modifier.padding(top = it.calculateTopPadding(), bottom = it.calculateBottomPadding())
                    )
                }
            }
        }
    }
}


@Composable
private fun Navigation(navController: NavHostController, modifier: Modifier) {
    NavHost(navController, startDestination = NavigationItem.Lists.route, modifier = modifier) {
        composable(NavigationItem.Lists.route) {
            ReorderList()
        }
        composable(NavigationItem.Grids.route) {
            ReorderGrid()
        }
        composable(NavigationItem.Fixed.route) {
            ReorderImageList()
        }
    }
}


@Composable
private fun BottomNavigationBar(items: List<NavigationItem>, navController: NavController) {
    BottomAppBar(contentColor = Color.White) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, item.title) },
                label = { Text(text = item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

private sealed class NavigationItem(var route: String, var icon: ImageVector, var title: String) {
    object Lists : NavigationItem("lists", Icons.Default.List, "Lists")
    object Grids : NavigationItem("grids", Icons.Default.Settings, "Grids")
    object Fixed : NavigationItem("fixed", Icons.Default.Star, "Fixed")
}