package com.shoplite.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shoplite.app.ui.theme.ShopLiteTheme

/**
 * Main entry point for the ShopLite app.
 * 
 * This activity hosts the Compose navigation graph
 * and aggregates all feature modules.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopLiteTheme {
                ShopLiteApp()
            }
        }
    }
}
