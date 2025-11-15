package com.example.musiletra.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musiletra.model.Song
import com.example.musiletra.ui.PlaylistViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailScreen(
    playlistId: String,
    playlistViewModel: PlaylistViewModel,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var playlistWithSongs by remember { mutableStateOf<com.example.musiletra.model.PlaylistWithSongs?>(null) }
    var showAddSongDialog by remember { mutableStateOf(false) }

    LaunchedEffect(playlistId) {
        playlistWithSongs = playlistViewModel.getPlaylistWithSongs(playlistId)
    }

    val playlist = playlistWithSongs?.playlist
    val songs = playlistWithSongs?.songs ?: emptyList()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(playlist?.name ?: "Playlist") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddSongDialog = true }) {
                Icon(Icons.Default.Add, "Adicionar Música")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            playlist?.let {
                if (it.description.isNotBlank()) {
                    Text(
                        text = it.description,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    HorizontalDivider()
                }
            }

            if (songs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Nenhuma música nesta playlist ainda.")
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(songs, key = { it.id }) { song ->
                        PlaylistSongItem(
                            song = song,
                            onRemove = {
                                scope.launch {
                                    playlistViewModel.removeSongFromPlaylist(playlistId, song.id)
                                    playlistWithSongs = playlistViewModel.getPlaylistWithSongs(playlistId)
                                }
                            }
                        )
                    }
                }
            }
        }

        if (showAddSongDialog) {
            AddSongToPlaylistDialog(
                availableSongs = playlistViewModel.availableSongs,
                currentSongIds = songs.map { it.id }.toSet(),
                onDismiss = { showAddSongDialog = false },
                onAddSong = { songId ->
                    scope.launch {
                        playlistViewModel.addSongToPlaylist(playlistId, songId)
                        playlistWithSongs = playlistViewModel.getPlaylistWithSongs(playlistId)
                    }
                }
            )
        }
    }
}

@Composable
fun PlaylistSongItem(
    song: Song,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                if (song.artist.isNotBlank()) {
                    Text(
                        text = song.artist,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = song.lyrics,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, "Remover da playlist")
            }
        }
    }
}

@Composable
fun AddSongToPlaylistDialog(
    availableSongs: List<Song>,
    currentSongIds: Set<String>,
    onDismiss: () -> Unit,
    onAddSong: (String) -> Unit
) {
    val songsToAdd = availableSongs.filter { it.id !in currentSongIds }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Música") },
        text = {
            if (songsToAdd.isEmpty()) {
                Text("Todas as músicas já estão nesta playlist.")
            } else {
                LazyColumn {
                    items(songsToAdd, key = { it.id }) { song ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            onClick = {
                                onAddSong(song.id)
                                onDismiss()
                            }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = song.title,
                                    fontWeight = FontWeight.Bold
                                )
                                if (song.artist.isNotBlank()) {
                                    Text(
                                        text = song.artist,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar")
            }
        }
    )
}
