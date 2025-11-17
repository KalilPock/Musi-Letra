package com.example.musiletra.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.musiletra.model.PlaylistWithSongs
import com.example.musiletra.model.Song
import com.example.musiletra.ui.viewmodels.PlaylistViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailScreen(
    playlistId: Int,
    playlistViewModel: PlaylistViewModel,
    onBack: () -> Unit
) {
    var playlistWithSongs by remember { mutableStateOf<PlaylistWithSongs?>(null) }
    var showAddSongDialog by remember { mutableStateOf(false) }

    val availableSongs by playlistViewModel.availableSongs.collectAsState()

    LaunchedEffect(playlistId, playlistViewModel.playlists) {
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
                        Icon(Icons.Default.ArrowBack, "Voltar")
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
                                playlistViewModel.removeSongFromPlaylist(playlistId, song.id)
                                playlistWithSongs = playlistViewModel.getPlaylistWithSongs(playlistId)
                            }
                        )
                    }
                }
            }
        }

        if (showAddSongDialog) {
            AddSongToPlaylistDialog(
                availableSongs = availableSongs,
                currentSongIds = songs.map { it.id }.toSet(),
                onDismiss = { showAddSongDialog = false },
                onAddSong = { songId ->
                    playlistViewModel.addSongToPlaylist(playlistId, songId)
                    playlistWithSongs = playlistViewModel.getPlaylistWithSongs(playlistId)
                }
            )
        }
    }
}

@Composable
fun PlaylistSongItem(
    song: Song,
    onRemove: suspend () -> Unit
) {
    val scope = rememberCoroutineScope()
    var isRemoving by remember { mutableStateOf(false) }

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
            IconButton(
                onClick = {
                    if (isRemoving) return@IconButton
                    scope.launch {
                        isRemoving = true
                        onRemove()
                    }
                },
                enabled = !isRemoving
            ) {
                if (isRemoving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Icon(Icons.Default.Delete, "Remover da playlist")
                }
            }
        }
    }
}

@Composable
fun AddSongToPlaylistDialog(
    availableSongs: List<Song>,
    currentSongIds: Set<String>,
    onDismiss: () -> Unit,
    onAddSong: suspend (String) -> Unit
) {
    val songsToAdd = availableSongs.filter { it.id !in currentSongIds }
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Música") },
        text = {
            if (songsToAdd.isEmpty()) {
                Text("Todas as músicas já estão nesta playlist.")
            } else {
                LazyColumn {
                    items(songsToAdd, key = { it.id }) { song ->
                        var isAdding by remember { mutableStateOf(false) }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            onClick = {
                                if (isAdding) return@Card
                                scope.launch {
                                    isAdding = true
                                    onAddSong(song.id)
                                }
                            }
                        ) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (isAdding) {
                                    CircularProgressIndicator(Modifier.size(24.dp))
                                } else {
                                    Column {
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
