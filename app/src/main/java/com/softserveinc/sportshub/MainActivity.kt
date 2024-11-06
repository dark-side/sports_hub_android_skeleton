package com.softserveinc.sportshub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.softserveinc.sportshub.ui.Main
import com.softserveinc.sportshub.ui.theme.SportsHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SportsHubTheme {
                Main(
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
