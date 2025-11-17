package com.example.musiletra.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musiletra.model.Playlist
import com.example.musiletra.model.Song
import com.example.musiletra.ui.viewmodels.PlaylistViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailScreen(
    playlistId: Int,
    playlistViewModel: PlaylistViewModel,
    onBack: () -> Unit,
    onInfo: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var playlist by remember { mutableStateOf<Playlist?>(null) }
    var songs by remember { mutableStateOf<List<Song>>(emptyList()) }
    var showAddSongDialog by remember { mutableStateOf(false) }

    LaunchedEffect(playlistId) {
        playlist = playlistViewModel.getPlaylist(playlistId)
        songs = playlistViewModel.getPlaylistSongs(playlistId)
    }

    // Update songs whenever playlists change
    LaunchedEffect(playlistViewModel.playlistsWithSongs) {
        songs = playlistViewModel.getPlaylistSongs(playlistId)
    }

    val currentPlaylist = playlist

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentPlaylist?.name ?: "Playlist") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = onInfo) {
                        Icon(Icons.Default.Info, "Informações")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddSongDialog = true },
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("Adicionar Música") },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            currentPlaylist?.let {
                if (!it.description.isNullOrBlank()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Text(
                            text = it.description!!,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            if (songs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Nenhuma música nesta playlist",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "Toque em + para adicionar",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(songs, key = { it.id }) { song ->
                        PlaylistSongItem(
                            song = song,
                            onRemove = {
                                scope.launch {
                                    playlistViewModel.removeSongFromPlaylist(playlistId, song.id)
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
                    }
                    showAddSongDialog = false
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
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (song.artist.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = song.artist,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = song.lyrics,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    lineHeight = 18.sp
                )
            }
            IconButton(
                onClick = onRemove,
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Default.Delete, "Remover", modifier = Modifier.size(20.dp))
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
        title = { Text("Adicionar Música à Playlist") },
        text = {
            if (songsToAdd.isEmpty()) {
                Text("Todas as músicas já estão nesta playlist.")
            } else {
                LazyColumn(modifier = Modifier.height(300.dp)) {
                    items(songsToAdd, key = { it.id }) { song ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            onClick = { onAddSong(song.id) },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = song.title,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                if (song.artist.isNotBlank()) {
                                    Text(
                                        text = song.artist,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
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
