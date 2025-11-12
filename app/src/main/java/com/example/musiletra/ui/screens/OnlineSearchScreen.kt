package com.example.musiletra.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musiletra.ui.viewmodels.SongViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlineSearchScreen(songViewModel: SongViewModel) {
    var query by remember { mutableStateOf("") }
    // Usar os resultados da busca online, não a lista de músicas locais
    val searchResults = songViewModel.onlineSearchResults

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
        // Chamar a função de busca do ViewModel
        Button(onClick = { songViewModel.searchOnline(query) }, modifier = Modifier.fillMaxWidth()) {
            Text("Search")
        }
        Spacer(Modifier.height(16.dp))

        // Exibir os resultados da busca online
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(searchResults, key = {"${it.title}-${it.artist}"}) { song ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(song.title, style = MaterialTheme.typography.titleMedium)
                        Text(song.artist, style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            song.lyrics,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 3
                        )
                        Spacer(Modifier.height(8.dp))
                        // Adicionar a música encontrada à lista local
                        Button(onClick = {
                            songViewModel.addSong(
                                title = song.title,
                                artist = song.artist,
                                lyrics = song.lyrics
                            )
                        }) {
                            Text("Add to my list")
                        }
                    }
                }
            }
        }
    }
}