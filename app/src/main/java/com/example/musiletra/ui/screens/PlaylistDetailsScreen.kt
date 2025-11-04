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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musiletra.model.Playlist
import com.example.musiletra.model.Song

private val allPlaylists = listOf(
    Playlist(
        1, "Rock Classics", songs = listOf(
            Song("s1", "Bohemian Rhapsody", "Queen", "Lyrics..."),
            Song("s2", "Stairway to Heaven", "Led Zeppelin", "Lyrics..."),
        )
    ),
    Playlist(
        2, "Lofi Beats to Study To", songs = listOf(
            Song("s3", "Affection", "Jinsang", "Lyrics..."),
            Song("s4", "Sunday Morning", "Trell Daniels", "Lyrics..."),
        )
    ),
    Playlist(3, "80s Pop Hits", songs = listOf()), // An empty playlist
    Playlist(
        4, "Acoustic Mornings", songs = listOf(
            Song("s5", "Fast Car", "Tracy Chapman", "Lyrics..."),
        )
    ),
)

@Composable
fun PlaylistDetailsScreen(
    playlistId: Int,
    modifier: Modifier = Modifier,
    onOpenSong: (String) -> Unit,
    onEditSong: (String) -> Unit,
    onDeleteSong: (String) -> Unit
) {
    val playlist = allPlaylists.find { it.id == playlistId }

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
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp) // Add some padding
        ) {
            items(playlist.songs, key = { it.id }) { song ->
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
