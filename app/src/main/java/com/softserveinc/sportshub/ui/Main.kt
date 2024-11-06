@file:OptIn(ExperimentalMaterial3Api::class)

package com.softserveinc.sportshub.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softserveinc.sportshub.R
import com.softserveinc.sportshub.ui.theme.SportsHubTheme
import kotlinx.coroutines.launch

@Preview
@Composable
fun MainPreview() {
    SportsHubTheme {
        Main()
    }
}

@Composable
fun Main(modifier: Modifier = Modifier) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showAccountDropdown by remember {
        mutableStateOf(false)
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerEntry.entries.forEachIndexed { index, it ->
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = stringResource(it.titleResId),
                                style = MaterialTheme.typography.titleMedium,
                            )
                        },
                        selected = index == 0,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    )
                }
            }
        },
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Sports Hub")
                    },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clickable {
                                    scope.launch {
                                        drawerState.open()
                                    }
                                },
                        )
                    },
                    actions = {
                        Box {
                            IconButton(
                                onClick = {
                                    showAccountDropdown = true
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                )
                            }
                            DropdownMenu(
                                expanded = showAccountDropdown,
                                onDismissRequest = {
                                    showAccountDropdown = false
                                },
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(text = "Sign up")
                                    },
                                    onClick = {
                                        showAccountDropdown = false
                                    },
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(text = "Log in")
                                    },
                                    onClick = {
                                        showAccountDropdown = false
                                    },
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        scrolledContainerColor = MaterialTheme.colorScheme.primary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            },
        ) { innerPadding ->

        }
    }
}

enum class DrawerEntry(
    val titleResId: Int,
) {
    HOME(R.string.title_home),
    TEAM_HUB(R.string.title_team_hub),
    LIFESTYLE(R.string.title_lifestyle),
    DEALBOOK(R.string.title_dealbook),
    VIDEO(R.string.title_video),
}
