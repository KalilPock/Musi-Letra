package com.example.musiletra.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musiletra.ui.viewmodels.SongViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSongScreen(
    songViewModel: SongViewModel,
    existingSongId: String? = null,
    onSave: () -> Unit, // Alterado para não precisar passar os parâmetros
    onCancel: () -> Unit
) {
    val existingSong = existingSongId?.let { id ->
        songViewModel.songs.find { it.id == id }
    }

    var title by remember { mutableStateOf(existingSong?.title ?: "") }
    var artist by remember { mutableStateOf(existingSong?.artist ?: "") }
    var lyrics by remember { mutableStateOf(existingSong?.lyrics ?: "") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = artist,
            onValueChange = { artist = it },
            label = { Text("Artista") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = lyrics,
            onValueChange = { lyrics = it },
            label = { Text("Letra") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            maxLines = 10
        )
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            TextButton(onClick = onCancel) { Text("Cancelar") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                if (title.isNotBlank() && lyrics.isNotBlank()) {
                    if (existingSongId != null) {
                        songViewModel.editSong(existingSongId, title, artist, lyrics)
                    } else {
                        songViewModel.addSong(title, artist, lyrics)
                    }
                    onSave() // Navega de volta após salvar
                }
            }) {
                Text("Salvar")
            }
        }
    }
}