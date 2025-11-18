package com.example.musiletra.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.musiletra.model.AudDSong

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlineSearchScreen(
    songs: List<AudDSong>,
    onSearch: (String) -> Unit,
    onAdd: (String, String, String) -> Unit,
    onBack: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    // Simula estado de busca
    LaunchedEffect(songs) {
        if (songs.isNotEmpty()) {
            isSearching = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buscar Letras Online", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
                .padding(16.dp)
        ) {
            // Campo de busca
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Digite parte da letra...") },
                placeholder = { Text("Ex: imagine all the people") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpar")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(12.dp))

            // Botão de busca
            Button(
                onClick = {
                    if (query.isNotBlank()) {
                        isSearching = true
                        onSearch(query)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = query.isNotBlank() && !isSearching
            ) {
                if (isSearching) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Icon(Icons.Default.CloudDownload, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(if (isSearching) "Buscando..." else "Buscar na Internet")
            }

            Spacer(Modifier.height(16.dp))

            // Info card
            if (songs.isEmpty() && !isSearching) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            "Digite um trecho da letra e clique em buscar para encontrar músicas online",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }

            // Loading state
            if (isSearching && songs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(16.dp))
                        Text("Buscando músicas online...")
                    }
                }
            }

            // Results list
            if (songs.isNotEmpty()) {
                Text(
                    "${songs.size} resultado${if (songs.size != 1) "s" else ""} encontrado${if (songs.size != 1) "s" else ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(Modifier.height(8.dp))
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(songs, key = { "${it.title}-${it.artist}" }) { song ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        song.title,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        song.artist,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                            }

                            Spacer(Modifier.height(12.dp))

                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Text(
                                    song.lyrics,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(12.dp),
                                    maxLines = 4,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(Modifier.height(12.dp))

                            Button(
                                onClick = { onAdd(song.title, song.artist, song.lyrics) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Adicionar à Minha Biblioteca")
                            }
                        }
                    }
                }
            }
        }
    }
}
