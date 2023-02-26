package com.yan.proto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.yan.proto.ui.theme.ProtoDataStoreTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val exampleCounterFlow: Flow<Int> by lazy {
        settingsDataStore.data
            .map { settings ->
                // The exampleCounter property is generated from the proto schema.
                settings.exampleCounter
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProtoDataStoreTheme {
                val counter by exampleCounterFlow.collectAsState(initial = 0)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android $counter", increment = { incrementCounter() })
                }
            }
        }
    }

    private fun incrementCounter() {
        lifecycleScope.launch {
            settingsDataStore.updateData { currentSettings ->
                currentSettings.toBuilder()
                    .setExampleCounter(currentSettings.exampleCounter + 1)
                    .build()
            }
        }
    }
}

@Composable
fun Greeting(name: String, increment: () -> Unit = {}) {
    Row {
        Text(text = "Hello $name!")
        Button(onClick = { increment() }) {
            Text(text = "IncrementCounter")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProtoDataStoreTheme {
        Greeting("Android")
    }
}