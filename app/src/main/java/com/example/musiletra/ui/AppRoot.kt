package com.example.musiletra.ui

import androidx.compose.runtime.Composable
import com.example.musiletra.ui.screens.AddEditSongScreen
import com.example.musiletra.ui.screens.OnlineSearchScreen
import com.example.musiletra.ui.screens.SongDetailScreen
import com.example.musiletra.ui.screens.SongListScreen

@Composable
fun AppRoot(viewModel: SongViewModel) {
    // Correctly get the state from the ViewModel
    val songs = viewModel.songs

    when (viewModel.currentScreen) {
        is Screen.List -> SongListScreen(
            songs = songs,
            onAdd = { viewModel.navigateToAdd() },
            onOpen = { id -> viewModel.openDetail(id) },
            onEdit = { id -> viewModel.navigateToEdit(id) },
            onDelete = { id -> viewModel.deleteSong(id) },
            onGoToSearch = { viewModel.navigateToOnlineSearch() }
        )
        is Screen.Add -> AddEditSongScreen(
            onSave = { t, a, l -> viewModel.addSong(t, a, l) },
            onCancel = { viewModel.navigateToList() }
        )
        is Screen.Edit -> {
            val id = viewModel.selectedSongId ?: return
            val song = songs.firstOrNull { it.id == id } ?: return
            AddEditSongScreen(
                existing = song,
                onSave = { t, a, l -> viewModel.editSong(id, t, a, l) },
                onCancel = { viewModel.navigateToList() }
            )
        }
        is Screen.Detail -> {
            val id = viewModel.selectedSongId ?: return
            val song = songs.firstOrNull { it.id == id } ?: return
            SongDetailScreen(
                song = song,
                onBack = { viewModel.navigateToList() },
                onEdit = { viewModel.navigateToEdit(id) },
                onDelete = { viewModel.deleteSong(id) }
            )
        }
        is Screen.OnlineSearch -> OnlineSearchScreen(
            songs = viewModel.onlineSearchResults,
            onSearch = { query -> viewModel.searchOnline(query) },
            onAdd = { title, artist, lyrics -> viewModel.addSong(title, artist, lyrics) },
            onBack = { viewModel.navigateToList() }
        )
    }
}
