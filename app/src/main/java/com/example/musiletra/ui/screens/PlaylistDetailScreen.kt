package com.example.musiletra.ui.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musiletra.data.database.MusicaSalva
import com.example.musiletra.data.database.Playlist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistDetailScreen(
    playlist: Playlist,
    allSongs: List<MusicaSalva>,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: (Int) -> Unit,
    onSongClick: (Int) -> Unit,
    onAddSong: (Int) -> Unit,
    onRemoveSong: (Int) -> Unit
) {
    val context = LocalContext.current
    var showAddSongDialog by remember { mutableStateOf(false) }

    val playlistSongIds = if (playlist.musicaIds.isEmpty()) {
        emptySet()
    } else {
        playlist.musicaIds.split(",").mapNotNull { it.toIntOrNull() }.toSet()
    }

    val playlistSongs = allSongs.filter { it.id in playlistSongIds }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(playlist.nome) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // Compartilhar playlist via Intent
                        val shareText = buildString {
                            appendLine("📋 ${playlist.nome}")
                            if (playlist.descricao.isNotBlank()) {
                                appendLine(playlist.descricao)
                            }
                            appendLine()
                            appendLine("Músicas:")
                            playlistSongs.forEach { song ->
                                appendLine("🎵 ${song.titulo} - ${song.artista}")
                            }
                            appendLine()
                            appendLine("Compartilhado do Musi-Letra")
                        }

                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, "Compartilhar playlist")
                        context.startActivity(shareIntent)
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Compartilhar")
                    }
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { onDelete(playlist.id) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Excluir")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddSongDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Música")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (playlist.descricao.isNotBlank()) {
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        playlist.descricao,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            if (playlistSongs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.MusicNote,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            "Playlist vazia",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Toque em + para adicionar músicas",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(playlistSongs, key = { it.id }) { song ->
                        PlaylistSongItem(
                            song,
                            onClick = { onSongClick(song.id) },
                            onRemove = { onRemoveSong(song.id) }
                        )
                    }
                }
            }
        }
    }

    if (showAddSongDialog) {
        val availableSongs = allSongs.filter { it.id !in playlistSongIds }

        AddSongToPlaylistDialog(
            songs = availableSongs,
            onDismiss = { showAddSongDialog = false },
            onAddSong = { songId ->
                onAddSong(songId)
                showAddSongDialog = false
            }
        )
    }
}

@Composable
fun PlaylistSongItem(song: MusicaSalva, onClick: () -> Unit, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() },
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
                    song.titulo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (song.artista.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        song.artista,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Remove,
                    contentDescription = "Remover",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun AddSongToPlaylistDialog(
    songs: List<MusicaSalva>,
    onDismiss: () -> Unit,
    onAddSong: (Int) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adicionar Música") },
        text = {
            if (songs.isEmpty()) {
                Text("Todas as músicas já estão na playlist ou você não tem músicas disponíveis.")
            } else {
                LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                    items(songs, key = { it.id }) { song ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { onAddSong(song.id) },
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    song.titulo,
                                    fontWeight = FontWeight.Medium
                                )
                                if (song.artista.isNotBlank()) {
                                    Text(
                                        song.artista,
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
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
