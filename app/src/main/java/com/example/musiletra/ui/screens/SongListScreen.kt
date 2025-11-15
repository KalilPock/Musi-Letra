package com.example.musiletra.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musiletra.model.Song

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongListScreen(
    songs: List<Song>,
    onAdd: () -> Unit,
    onOpen: (String) -> Unit,
    onEdit: (String) -> Unit,
    onDelete: (String) -> Unit,
    onGoToSearch: () -> Unit,
    onGoToPlaylists: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Musiletras") },
                actions = {
                    IconButton(onClick = onGoToPlaylists) {
                        Icon(Icons.Default.PlaylistPlay, contentDescription = "Playlists")
                    }
                    IconButton(onClick = onGoToSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Search Online")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar")
            }
        }
    ) { padding ->
        if (songs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhuma música adicionada ainda.")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
                items(songs, key = { it.id }) { song ->
                    SongItem(
                        song,
                        onOpen = { onOpen(song.id) },
                        onEdit = { onEdit(song.id) },
                        onDelete = { onDelete(song.id) })
                }
            }
        }
    }
}

@Composable
fun SongItem(song: Song, onOpen: () -> Unit, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onOpen() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(song.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    if (song.artist.isNotBlank()) Text(song.artist, fontSize = 14.sp)
                }
                Row {
                    IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription = "Editar") }
                    IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Excluir") }
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(song.lyrics, maxLines = 2, overflow = TextOverflow.Ellipsis)
        }
    }
}
