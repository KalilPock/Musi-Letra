package com.example.musiletra.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musiletra.data.AudDSong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlineSearchScreen(
    songs: List<AudDSong>,
    onSearch: (String) -> Unit,
    onAdd: (String, String, String) -> Unit,
    onBack: () -> Unit
) {
    var query by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Online") },
                navigationIcon = { IconButton(onClick = onBack) { Text("←") } }
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).padding(8.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Enter lyrics...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))
            Button(onClick = { onSearch(query) }, modifier = Modifier.fillMaxWidth()) {
                Text("Search")
            }
            Spacer(Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(songs) { song ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(song.title, style = MaterialTheme.typography.titleMedium)
                            Text(song.artist, style = MaterialTheme.typography.bodySmall)
                            Spacer(Modifier.height(8.dp))
                            Text(song.lyrics, style = MaterialTheme.typography.bodyMedium, maxLines = 3)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = { onAdd(song.title, song.artist, song.lyrics) }) {
                                Text("Add to my list")
                            }
                        }
                    }
                }
            }
        }
    }
}