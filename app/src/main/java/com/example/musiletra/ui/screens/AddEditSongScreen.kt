package com.example.musiletra.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.musiletra.data.database.MusicaSalva

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSongScreen(
    existingSong: MusicaSalva? = null,
    onSave: (String, String, String) -> Unit,
    onCancel: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf(existingSong?.titulo ?: "") }
    var artist by rememberSaveable { mutableStateOf(existingSong?.artista ?: "") }
    var lyrics by rememberSaveable { mutableStateOf(existingSong?.letra ?: "") }

    val isEditMode = existingSong != null
    val canSave = title.isNotBlank() && lyrics.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditMode) "Editar Música" else "Nova Música",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Info Card
            if (!isEditMode) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            "Preencha os campos abaixo para adicionar uma nova música",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Campo Título
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título *") },
                    placeholder = { Text("Ex: Imagine") },
                    leadingIcon = {
                        Icon(Icons.Default.MusicNote, contentDescription = null)
                    },
                    trailingIcon = {
                        if (title.isNotEmpty()) {
                            IconButton(onClick = { title = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpar")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = title.isEmpty() && lyrics.isNotEmpty(),
                    supportingText = {
                        if (title.isEmpty() && lyrics.isNotEmpty()) {
                            Text("Título é obrigatório")
                        }
                    }
                )

                // Campo Artista
                OutlinedTextField(
                    value = artist,
                    onValueChange = { artist = it },
                    label = { Text("Artista") },
                    placeholder = { Text("Ex: John Lennon") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    trailingIcon = {
                        if (artist.isNotEmpty()) {
                            IconButton(onClick = { artist = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Limpar")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Campo Letra
                OutlinedTextField(
                    value = lyrics,
                    onValueChange = { lyrics = it },
                    label = { Text("Letra *") },
                    placeholder = { Text("Digite ou cole a letra da música...") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Description,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Top).padding(top = 12.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp),
                    maxLines = Int.MAX_VALUE,
                    isError = lyrics.isEmpty() && title.isNotEmpty(),
                    supportingText = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (lyrics.isEmpty() && title.isNotEmpty()) {
                                Text("Letra é obrigatória")
                            } else {
                                Text("Campo obrigatório")
                            }
                            Text("${lyrics.length} caracteres")
                        }
                    }
                )

                Spacer(Modifier.height(8.dp))

                // Botões de Ação
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            if (canSave) {
                                onSave(title.trim(), artist.trim(), lyrics.trim())
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = canSave,
                        contentPadding = PaddingValues(vertical = 14.dp)
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(if (isEditMode) "Atualizar" else "Salvar")
                    }
                }

                // Info sobre campos obrigatórios
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "* Campos obrigatórios",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}
