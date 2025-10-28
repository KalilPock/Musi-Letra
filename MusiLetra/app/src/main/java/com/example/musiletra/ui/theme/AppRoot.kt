package com.example.musiletra.ui

import androidx.compose.runtime.*
import com.example.musiletra.ui.screens.*

@Composable
fun AppRoot(viewModel: SongViewModel) {
    when (val screen = viewModel.currentScreen) {
        is Screen.List -> SongListScreen(
            songs = viewModel.songs,
            onAdd = { viewModel.navigateToAdd() },
            onSearch = { viewModel.navigateToSearch() },
            onOpen = { id -> viewModel.openDetail(id) },
            onEdit = { id -> viewModel.navigateToEdit(id) },
            onDelete = { id -> viewModel.deleteSong(id) }
        )
        
        is Screen.Search -> SearchScreen(
            isSearching = viewModel.isSearching,
            searchResults = viewModel.searchResults,
            searchError = viewModel.searchError,
            onSearch = { query -> viewModel.searchByText(query) },
            onSelectSong = { songItem ->
                // Adiciona a música diretamente da busca
                viewModel.addSongFromSearch(songItem)
            },
            onClearError = { viewModel.clearSearchError() },
            onBack = { viewModel.navigateToList() }
        )
        
        is Screen.Add -> AddEditSongScreen(
            isLoadingLyrics = viewModel.isLoadingLyrics,
            lyricError = viewModel.lyricError,
            onSearchLyrics = { artist, title ->
                viewModel.fetchLyrics(artist, title) { lyrics ->
                    // A letra será preenchida automaticamente
                }
            },
            onClearError = { viewModel.clearLyricError() },
            onSave = { t, a, l -> viewModel.addSong(t, a, l) },
            onCancel = { viewModel.navigateToList() }
        )
        
        is Screen.Edit -> {
            val id = viewModel.selectedSongId ?: return
            val song = viewModel.songs.firstOrNull { it.id == id } ?: return
            AddEditSongScreen(
                existing = song,
                isLoadingLyrics = viewModel.isLoadingLyrics,
                lyricError = viewModel.lyricError,
                onSearchLyrics = { artist, title ->
                    viewModel.fetchLyrics(artist, title) { lyrics ->
                        // Letra atualizada
                    }
                },
                onClearError = { viewModel.clearLyricError() },
                onSave = { t, a, l -> viewModel.editSong(id, t, a, l) },
                onCancel = { viewModel.navigateToList() }
            )
        }
        
        is Screen.Detail -> {
            val id = viewModel.selectedSongId ?: return
            val song = viewModel.songs.firstOrNull { it.id == id } ?: return
            SongDetailScreen(
                song = song,
                onBack = { viewModel.navigateToList() },
                onEdit = { viewModel.navigateToEdit(id) },
                onDelete = { viewModel.deleteSong(id) }
            )
        }
    }
}