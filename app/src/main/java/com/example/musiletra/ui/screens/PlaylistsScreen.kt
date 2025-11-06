package com.example.musiletra.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable // 1. Import clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues // 2. For better spacing
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio // 3. For better image scaling
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip // 4. For clipping the image to the card shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.musiletra.R
import com.example.musiletra.model.Playlist
import com.example.musiletra.ui.viewmodels.PlaylistsViewModel

@Composable
fun PlaylistsScreen(navController: NavController, viewModel : PlaylistsViewModel = PlaylistsViewModel()) {

//  Força a chamada de renderização do viewModel para ser executada uma única vez quando a tela é criada
    LaunchedEffect(Unit) {
        viewModel.startLoadingPlaylists()
    }
    val playlists = viewModel.playlist.value

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(playlists, key = { it.id }) { playlist -> // 6. Add a key for performance
            PlaylistItem(
                playlist = playlist, onClick = {
                    navController.navigate("playlist_details/${playlist.id}")
                })
        }
    }
}

@Composable
fun PlaylistItem(playlist: Playlist, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Image(
                painter = painterResource(R.drawable.imagem_playlist),
                contentDescription = "Imagem da Playlist de ${playlist.name}",
                contentScale = ContentScale.Crop, // Ensures the image fills the space
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium)
            )

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    text = playlist.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis // Handle long playlist names
                )
                Text(
                    text = "${playlist.songs.size} músicas",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Info, contentDescription = "Informações")
                Icon(Icons.Default.Share, contentDescription = "Compartilhar")
            }
        }
    }
}
