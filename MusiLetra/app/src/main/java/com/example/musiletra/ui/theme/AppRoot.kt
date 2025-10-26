package com.example.musiletra.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiletra.data.SongRepository
import com.example.musiletra.model.Song
import com.example.musiletra.ui.screens.AddEditSongScreen
import com.example.musiletra.ui.screens.SongDetailScreen
import com.example.musiletra.ui.screens.SongListScreen
import kotlinx.coroutines.launch

sealed class Screen {
    object List : Screen()
    object Add : Screen()
    object Edit : Screen()
    object Detail : Screen()
}



class SongViewModel : ViewModel() {
    var songs by mutableStateOf<List<Song>>(emptyList())
        private set

    var currentScreen by mutableStateOf<Screen>(Screen.List)
        private set

    var selectedSongId by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            SongRepository.songs.collect { songs = it }
        }
    }

    fun addSong(title: String, artist: String, lyrics: String) {
        SongRepository.add(Song(title = title, artist = artist, lyrics = lyrics))
        navigateToList()
    }

    fun editSong(id: String, title: String, artist: String, lyrics: String) {
        val updated = SongRepository.get(id)?.copy(title = title, artist = artist, lyrics = lyrics)
        if (updated != null) SongRepository.update(updated)
        navigateToList()
    }

    fun deleteSong(id: String) {
        SongRepository.delete(id)
        navigateToList()
    }

    fun openDetail(id: String) {
        selectedSongId = id
        currentScreen = Screen.Detail
    }

    fun navigateToAdd() { currentScreen = Screen.Add }
    fun navigateToEdit(id: String) { selectedSongId = id; currentScreen = Screen.Edit }
    fun navigateToList() { currentScreen = Screen.List; selectedSongId = null }
}


@Composable
fun AppRoot(viewModel: SongViewModel) {
    when (val screen = viewModel.currentScreen) {
        is Screen.List -> SongListScreen(
            songs = viewModel.songs,
            onAdd = { viewModel.navigateToAdd() },
            onOpen = { id -> viewModel.openDetail(id) },
            onEdit = { id -> viewModel.navigateToEdit(id) },
            onDelete = { id -> viewModel.deleteSong(id) }
        )
        is Screen.Add -> AddEditSongScreen(
            onSave = { t, a, l -> viewModel.addSong(t, a, l) },
            onCancel = { viewModel.navigateToList() }
        )
        is Screen.Edit -> {
            val id = viewModel.selectedSongId ?: return
            val song = viewModel.songs.firstOrNull { it.id == id } ?: return
            AddEditSongScreen(
                existing = song,
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
