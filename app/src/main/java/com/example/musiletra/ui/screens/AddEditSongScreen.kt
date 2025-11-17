package com.example.musiletra.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.musiletra.model.Song

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSongScreen(
    existing: Song? = null,
    onSave: (title: String, artist: String, lyrics: String) -> Unit,
    onCancel: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf(existing?.title ?: "") }
    var artist by rememberSaveable { mutableStateOf(existing?.artist ?: "") }
    var lyrics by rememberSaveable { mutableStateOf(existing?.lyrics ?: "") }

    val isEditing = existing != null
    val titleText = if (isEditing) "Editar Música" else "Nova Música"
    val canSave = title.isNotBlank() && lyrics.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        titleText,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, "Voltar")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (canSave) {
                                onSave(title.trim(), artist.trim(), lyrics.trim())
                            }
                        },
                        enabled = canSave
                    ) {
                        Icon(
                            Icons.Default.Save,
                            contentDescription = "Salvar",
                            tint = if (canSave) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Título Field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título *") },
                placeholder = { Text("Digite o título da música") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Title,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = title.isBlank() && title.isNotEmpty(),
                supportingText = {
                    if (title.isBlank() && title.isNotEmpty()) {
                        Text("O título é obrigatório")
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            // Artista Field
            OutlinedTextField(
                value = artist,
                onValueChange = { artist = it },
                label = { Text("Artista") },
                placeholder = { Text("Digite o nome do artista (opcional)") },
                leadingIcon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            // Letra Field
            OutlinedTextField(
                value = lyrics,
                onValueChange = { lyrics = it },
                label = { Text("Letra *") },
                placeholder = { Text("Digite a letra da música aqui...") },
                leadingIcon = {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        Icon(
                            Icons.Default.MusicNote,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 250.dp),
                minLines = 10,
                maxLines = 20,
                isError = lyrics.isBlank() && lyrics.isNotEmpty(),
                supportingText = {
                    if (lyrics.isBlank() && lyrics.isNotEmpty()) {
                        Text("A letra é obrigatória")
                    } else {
                        Text("${lyrics.length} caracteres")
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Save Button
            Button(
                onClick = {
                    if (canSave) {
                        onSave(title.trim(), artist.trim(), lyrics.trim())
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = canSave,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (isEditing) "Salvar Alterações" else "Adicionar Música",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Cancel Button
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    "Cancelar",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
