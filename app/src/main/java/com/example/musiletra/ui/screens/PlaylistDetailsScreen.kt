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
import androidx.compose.runtime.collectAsState // <-- NOVO IMPORT
import androidx.compose.runtime.getValue // <-- NOVO IMPORT
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musiletra.data.database.MusicaSalva // <-- NOVO IMPORT DO MODELO ROOM
import com.example.musiletra.ui.viewmodels.PlaylistsViewModel
import com.example.musiletra.ui.viewmodels.SongViewModel

@Composable
fun PlaylistDetailsScreen(
    playlistId: Int,
    playlistsViewModel: PlaylistsViewModel,
    songViewModel: SongViewModel,
    modifier: Modifier = Modifier,
    // --- MUDANÇA 1: Assinaturas usam Int ou MusicaSalva ---
    onOpenSong: (Int) -> Unit,
    onEditSong: (Int) -> Unit,
    onDeleteSong: (MusicaSalva) -> Unit // <-- DEVE RECEBER O OBJETO
) {
    // --- MUDANÇA 2: Coletar o StateFlow ---
    // Isto transforma o Flow em List<MusicaSalva>
    val songsList by songViewModel.songs.collectAsState()

    // --- MUDANÇA 3: Simplificar o uso da lista ---
    // Esta é a lista final de músicas do usuário (o SongViewModel já filtrou)
    val playlistSongs = songsList

    // O código da playlist antiga (linhas 32 a 45) foi comentado/removido para simplificação.

    if (playlistSongs.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Outlined.Save, contentDescription = "Nada salvo")
                Text(
                    "Não há nada salvo nesta playlist...", modifier = Modifier.padding(top = 8.dp) // Correção do padding
                )
            }
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            // Os items agora são MusicaSalva (o SongItem deve ser corrigido no Passo 4)
            items(playlistSongs, key = { it.id }) { song ->
                SongItem(
                    song = song,
                    onOpen = {
                        onOpenSong(song.id) // song.id agora é Int
                    },
                    onEdit = {
                        onEditSong(song.id) // song.id agora é Int
                    },
                    // --- MUDANÇA 4: Passar o objeto para deletar ---
                    onDelete = {
                        onDeleteSong(song) // Passa o objeto MusicaSalva inteiro
                    }
                )
            }
        }
    }
}