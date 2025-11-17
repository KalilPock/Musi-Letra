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
import com.example.musiletra.data.database.Playlist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditPlaylistScreen(
    existingPlaylist: Playlist? = null,
    onSave: (String, String) -> Unit,
    onCancel: () -> Unit
) {
    var nome by remember { mutableStateOf(existingPlaylist?.nome ?: "") }
    var descricao by remember { mutableStateOf(existingPlaylist?.descricao ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (existingPlaylist == null) "Nova Playlist" else "Editar Playlist") },
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
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome da Playlist") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição (opcional)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 5
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
                        if (nome.isNotBlank()) {
                            onSave(nome, descricao)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = nome.isNotBlank()
                ) {
                    Text("Salvar")
                }
            }
        }
    }
}
