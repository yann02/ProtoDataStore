package com.yan.proto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.yan.proto.ui.theme.ProtoDataStoreTheme
import kotlinx.coroutines.cancel
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

    private val animalNameFlow: Flow<String> by lazy {
        dataStore.data
            .map { preferences ->
                // No type safety.
                preferences[EXAMPLE_COUNTER] ?: "Ant"
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProtoDataStoreTheme {
                val counter by exampleCounterFlow.collectAsState(initial = 0)
                val animalName by animalNameFlow.collectAsState(initial = "Ant")
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        UsingProto("Android $counter", increment = { incrementCounter() })
                        UsingPreference(name = animalName, setName = {
                            updatingAnimalName(it)
                        })
                    }
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

    private fun updatingAnimalName(name: String) {
        lifecycleScope.launch {
            dataStore.edit { settings ->
                settings[EXAMPLE_COUNTER] = name
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.cancel()
    }
}

@Composable
fun UsingProto(name: String, increment: () -> Unit = {}) {
    Row {
        Text(text = "Hello $name!")
        Button(onClick = { increment() }) {
            Text(text = "IncrementCounter")
        }
    }
}

@Composable
fun UsingPreference(name: String, setName: (String) -> Unit = {}) {
    Row {
        var value by remember {
            mutableStateOf("")
        }
        LaunchedEffect(Unit) {
            value = name
        }
        TextField(value = value, onValueChange = { value = it }, modifier = Modifier.weight(1f))
        Text(text = name, modifier = Modifier.weight(1f), color = Color.Green)
        Button(onClick = { setName(value) }, modifier = Modifier.weight(1f)) {
            Text(text = "Updating Name")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProtoDataStoreTheme {
        UsingProto("Android")
    }
}