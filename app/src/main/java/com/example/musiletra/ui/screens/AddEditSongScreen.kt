package com.example.musiletra.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.musiletra.data.database.MusicaSalva

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSongScreen(
    existingSong: MusicaSalva? = null,
    onSave: (String, String, String) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf(existingSong?.titulo ?: "") }
    var artist by remember { mutableStateOf(existingSong?.artista ?: "") }
    var lyrics by remember { mutableStateOf(existingSong?.letra ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existingSong == null) "Adicionar Música" else "Editar Música") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = artist,
                onValueChange = { artist = it },
                label = { Text("Artista") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = lyrics,
                onValueChange = { lyrics = it },
                label = { Text("Letra") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                maxLines = 15
            )
            Spacer(Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancelar")
                }
                Button(
                    onClick = {
                        if (title.isNotBlank() && lyrics.isNotBlank()) {
                            onSave(title, artist, lyrics)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = title.isNotBlank() && lyrics.isNotBlank()
                ) {
                    Text("Salvar")
                }
            }
        }
    }
}
