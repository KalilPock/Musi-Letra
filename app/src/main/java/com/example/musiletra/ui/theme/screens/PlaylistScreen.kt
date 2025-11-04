package com.example.musiletra.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.example.musiletra.R
import com.example.musiletra.model.Song

class PlaylistViewModel : ViewModel() {
    private val _songs = mutableStateListOf<Song>()
    val songs: List<Song> = _songs

    fun addSong(song: Song) {
        _songs.add(song)
    }

    fun removeSong(song: Song) {
        _songs.remove(song)
    }
}

@Composable
fun PlaylistsScreen(viewModel: PlaylistViewModel) {
    Surface() {
        PlaylistItem()
    }
}

@Composable
fun PlaylistItem(modifier: Modifier = Modifier) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.imagem_playlist),
                contentDescription = "Imagem da Playlist"
            )

            Row {
                IconButton(onClick = {}) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                    Icon(Icons.Default.Info, contentDescription = "Informações")
                    Icon(Icons.Default.Share, contentDescription = "Compartilhar")
                }
            }
        }
    }
}
