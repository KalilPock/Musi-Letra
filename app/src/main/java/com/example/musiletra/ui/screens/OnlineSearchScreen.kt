package com.example.musiletra.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musiletra.model.Song

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlineSearchScreen() {
    var songs = arrayOf<Song>(
        Song("1", "1", "1", "1"),
        Song("2", "2", "2", "2"),
        Song("3", "3", "3", "3"),
    )
    var query by remember { mutableStateOf("") }
    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Enter lyrics...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(Modifier.height(8.dp))
        Button(onClick = { }, modifier = Modifier.fillMaxWidth()) {
            Text("Search")
        }
        Spacer(Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(songs) { song ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("title", style = MaterialTheme.typography.titleMedium)
                        Text("Artist", style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 3
                        )
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { }) {
                            Text("Add to my list")
                        }
                    }
                }
            }
        }
    }
}