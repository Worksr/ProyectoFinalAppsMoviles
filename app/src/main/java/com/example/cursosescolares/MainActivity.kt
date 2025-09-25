package com.example.cursosescolares

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.cursosescolares.ui.Navigation.AppNav
import com.example.cursosescolares.ui.theme.CursosEscolaresTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CursosEscolaresTheme {
                val nav = androidx.navigation.compose.rememberNavController()
                androidx.compose.material3.Surface(
                    modifier = androidx.compose.ui.Modifier.fillMaxSize()
                ) {
                    AppNav(nav)
                }
            }
        }
    }
}