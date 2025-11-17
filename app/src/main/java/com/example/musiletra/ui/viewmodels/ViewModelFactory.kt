package com.example.musiletra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musiletra.data.PlaylistRepository
import com.example.musiletra.data.SongRepository

class ViewModelFactory(
    private val songRepository: SongRepository,
    private val playlistRepository: PlaylistRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SongViewModel::class.java) -> {
                SongViewModel(songRepository) as T
            }
            modelClass.isAssignableFrom(PlaylistViewModel::class.java) -> {
                PlaylistViewModel(playlistRepository, songRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
