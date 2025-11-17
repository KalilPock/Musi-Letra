package com.example.musiletra.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.musiletra.data.database.MusicaSalva

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongDetailScreen(
    song: MusicaSalva,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDelete: (Int) -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(song.titulo) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        // Compartilhar música via Intent
                        val shareText = buildString {
                            appendLine("🎵 ${song.titulo}")
                            if (song.artista.isNotBlank()) {
                                appendLine("🎤 ${song.artista}")
                            }
                            appendLine()
                            appendLine(song.letra)
                            appendLine()
                            appendLine("Compartilhado do Musi-Letra")
                        }

                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }

                        val shareIntent = Intent.createChooser(sendIntent, "Compartilhar música")
                        context.startActivity(shareIntent)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Compartilhar"
                        )
                    }
                    IconButton(onClick = onEdit) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar")
                    }
                    IconButton(onClick = { onDelete(song.id) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Excluir")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (song.artista.isNotBlank()) {
                Text(
                    song.artista,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(16.dp))
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    song.letra,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
