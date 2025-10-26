package com.example.musiletra.data


import com.example.musiletra.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SongRepository {
    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs

    fun add(song: Song) {
        _songs.value = _songs.value + song
    }

    fun update(updated: Song) {
        _songs.value = _songs.value.map { if (it.id == updated.id) updated else it }
    }

    fun delete(id: String) {
        _songs.value = _songs.value.filterNot { it.id == id }
    }

    fun get(id: String): Song? = _songs.value.firstOrNull { it.id == id }
}
