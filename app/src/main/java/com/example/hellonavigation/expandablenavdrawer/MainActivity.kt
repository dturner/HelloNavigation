package com.example.hellonavigation.expandablenavdrawer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hellonavigation.ui.theme.HelloNavigationTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            HelloNavigationTheme {

                val drawerState = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()
// icons to mimic drawer destinations
                val items =
                    listOf(
                        Icons.Default.AccountCircle,

                    )
                val selectedItem = remember { mutableStateOf(items[0]) }
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            Column(Modifier.verticalScroll(rememberScrollState())) {
                                Spacer(Modifier.height(12.dp))
                                items.forEach { item ->
                                    NavigationDrawerItem(
                                        icon = { Icon(item, contentDescription = null) },
                                        label = { Text(item.name.substringAfterLast(".")) },
                                        selected = item == selectedItem.value,
                                        onClick = {
                                            scope.launch { drawerState.close() }
                                            selectedItem.value = item
                                        },
                                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                                    )
                                }
                            }
                        }
                    },
                    content = {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = if (drawerState.isClosed) ">>> Swipe >>>" else "<<< Swipe <<<")
                            Spacer(Modifier.height(20.dp))
                            Button(onClick = { scope.launch { drawerState.open() } }) { Text("Click to open") }
                        }
                    }
                )
                //NavigationDrawer()
            }
        }
    }
}

@Composable
fun NavigationDrawerGroup(
    label: @Composable () -> Unit,
    isExpanded: Boolean = true,
    onExpandedChange: (Boolean) -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    NavigationDrawerItem(
        label = label,
        selected = isExpanded,
        onClick = { onExpandedChange(!isExpanded) },
        icon = {
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Collapse" else "Expand"
            )
        }
    )
    AnimatedVisibility(visible = isExpanded) {
        Column(modifier = Modifier.padding(start = 16.dp)) {
            content()
        }
    }
}


enum class NavigationItem {
    DASHBOARD, PROFILE, PRIVACY, ABOUT
}


@Composable
fun DrawerContent(modifier: Modifier = Modifier) {
    var expandedGroup by remember { mutableStateOf<Int?>(null) }
    var selectedItem by remember { mutableStateOf<NavigationItem?>(null) }

    DismissibleDrawerSheet {
        NavigationDrawerGroup(
            label = { Text("Home", style = MaterialTheme.typography.titleMedium) },
            isExpanded = expandedGroup == 0,
            onExpandedChange = {
                expandedGroup =
                    if (it) 0 else null
            }
        ) {
            NavigationDrawerItem(
                label = { Text("Dashboard") },
                selected = selectedItem == NavigationItem.DASHBOARD,
                onClick = { selectedItem = NavigationItem.DASHBOARD }
            )
            NavigationDrawerItem(
                label = { Text("Profile") },
                selected = selectedItem == NavigationItem.PROFILE,
                onClick = { selectedItem = NavigationItem.PROFILE }
            )
        }
        NavigationDrawerGroup(
            label = { Text("Settings", style = MaterialTheme.typography.titleMedium) },
            isExpanded = expandedGroup == 1,
            onExpandedChange = {
                expandedGroup =
                    if (it) 1 else null
            }
        ) {
            NavigationDrawerItem(
                label = { Text("Privacy Policy") },
                selected = selectedItem == NavigationItem.PRIVACY,
                onClick = { selectedItem = NavigationItem.PRIVACY }
            )
            NavigationDrawerItem(
                label = { Text("About") },
                selected = selectedItem == NavigationItem.ABOUT,
                onClick = { selectedItem = NavigationItem.ABOUT }
            )
        }
    }
}


@Composable
fun NavigationDrawer() {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    DismissibleNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent() }
    ) {
        // Screen content
        Scaffold { contentPadding ->
            Column(modifier = Modifier.padding(contentPadding)) {
                Text("Slide to open the drawer", color = Color.Black)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewNestedNavigationDrawer() {
    NavigationDrawer()
}



