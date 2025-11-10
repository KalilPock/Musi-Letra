package com.example.musiletra.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musiletra.ui.viewmodels.PlaylistsViewModel
import com.example.musiletra.ui.viewmodels.SongViewModel

@Composable
fun PlaylistDetailsScreen(
    playlistId: Int,
    playlistsViewModel: PlaylistsViewModel,
    songViewModel: SongViewModel,
    modifier: Modifier = Modifier,
    onOpenSong: (String) -> Unit,
    onEditSong: (String) -> Unit,
    onDeleteSong: (String) -> Unit
) {
    val playlist = playlistsViewModel.playlists.find { it.id == playlistId }

    if (playlist?.songs.isNullOrEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column {
                Icon(Icons.Outlined.Save, contentDescription = "Nada salvo")
                Text(
                    "Não há nada salvo nesta playlist...", modifier = Modifier.padding(top = 80.dp)
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            val playlistSongs = songViewModel.songs.filter { playlist.songs.contains(it.id)}

            items(playlistSongs, key = { it.id }) { song ->
                SongItem(
                    song = song,

                    onOpen = {
                        onOpenSong(song.id)
                    }, onEdit = {
                        onEditSong(song.id)
                    }, onDelete = {
                        onDeleteSong(song.id)
                    })
            }
        }
    }
}
