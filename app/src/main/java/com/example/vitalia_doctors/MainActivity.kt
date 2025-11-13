package com.example.vitalia_doctors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.vitalia_doctors.ui.theme.Vitalia_doctorsTheme
import com.example.vitalia_doctors.views.nav.Navi

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Vitalia_doctorsTheme {
                Navi(this)
            }
        }
    }
}
