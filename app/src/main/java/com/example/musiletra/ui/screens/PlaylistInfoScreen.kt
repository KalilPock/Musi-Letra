package com.example.musiletra.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.musiletra.ui.viewmodels.PlaylistsViewModel

@Composable
fun PlaylistInfoScreen(
    navController: NavController, idPlaylist: Int, viewModel: PlaylistsViewModel
) {
    // 1. Obtenha a playlist de forma segura, sem usar '!!'
    val playlist = viewModel.getPlaylist(idPlaylist)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        if (playlist == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Playlist não encontrada ou carregando...")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { navController.popBackStack() }) {
                        Text("VOLTAR")
                    }
                }
            }
        } else {
            // Se a playlist existir, mostre as informações.
            Column(
                modifier = Modifier
                    .fillMaxSize() // A Coluna preenche o Card
                    .padding(16.dp) // Padding interno
            ) {
                // Informações da playlist
                Text(
                    text = "Playlist: ${playlist.name}",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (playlist.description.isNullOrEmpty()) {
                    "Sem descrição"
                } else {
                    playlist.description
                }?.let {
                    Text(
                        text = it, style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Quantidade de músicas: ${playlist.songs.size}",
                    style = MaterialTheme.typography.bodyMedium
                )

                // Este Spacer empurra os botões para a parte inferior
                Spacer(modifier = Modifier.weight(1f))

                // Linha de botões
                PlaylistActionButtons(
                    onNavigateBack = { navController.navigate("playlists/${playlist.id}") },
                    onShare = { /* TODO: Handle share */ },
                    onDelete = {
                        viewModel.deletePlaylist(playlist.id)
                        navController.popBackStack()
                    })
            }
        }
    }
}

@Composable
private fun PlaylistActionButtons(
    onNavigateBack: () -> Unit, onShare: () -> Unit, onDelete: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = onNavigateBack) { // Modificado para voltar, que parece mais lógico
            Text("VOLTAR")
        }
        IconButton(onClick = onShare) {
            Icon(Icons.Default.Share, contentDescription = "Compartilhar")
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Excluir")
        }
    }
}
