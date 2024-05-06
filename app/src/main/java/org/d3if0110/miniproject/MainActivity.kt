package org.d3if0110.miniproject

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import org.d3if0110.miniproject.navigation.SetupNavGraph
import org.d3if0110.miniproject.ui.theme.MiniProjectTheme
import org.d3if0110.miniproject.util.SettingsDataStore

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val dataStore = SettingsDataStore(LocalContext.current)
            val isDark by dataStore.layoutFlow.collectAsState(false)
            MiniProjectTheme(
                darkTheme = isDark
            ) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupNavGraph(isDark = isDark)
                }
            }
        }
    }
}
