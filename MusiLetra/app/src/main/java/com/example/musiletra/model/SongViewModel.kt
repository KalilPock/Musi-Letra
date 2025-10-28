package com.example.musiletra.ui

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiletra.data.LyricsRepository
import com.example.musiletra.data.SongRepository
import com.example.musiletra.model.AuddSongItem
import com.example.musiletra.model.Song
import kotlinx.coroutines.launch

sealed class Screen {
    object List : Screen()
    object Add : Screen()
    object Edit : Screen()
    object Detail : Screen()
    object Search : Screen()
}

class SongViewModel : ViewModel() {
    private val lyricsRepository = LyricsRepository()
    
    var songs by mutableStateOf<List<Song>>(emptyList())
        private set

    var currentScreen by mutableStateOf<Screen>(Screen.List)
        private set

    var selectedSongId by mutableStateOf<String?>(null)
        private set

    // Estados para busca
    var isSearching by mutableStateOf(false)
        private set
    
    var searchResults by mutableStateOf<List<AuddSongItem>>(emptyList())
        private set
    
    var searchError by mutableStateOf<String?>(null)
        private set

    // Estados para busca de letra específica
    var isLoadingLyrics by mutableStateOf(false)
        private set
    
    var lyricError by mutableStateOf<String?>(null)
        private set

    init {
        viewModelScope.launch {
            SongRepository.songs.collect { songs = it }
        }
    }

    /**
     * Busca músicas por texto (artista, título ou trecho)
     */
    fun searchByText(query: String) {
        if (query.isBlank()) {
            searchError = "Digite algo para buscar"
            return
        }

        viewModelScope.launch {
            isSearching = true
            searchError = null
            searchResults = emptyList()
            
            lyricsRepository.searchByText(query)
                .onSuccess { results ->
                    searchResults = results
                }
                .onFailure { error ->
                    searchError = error.message ?: "Erro desconhecido"
                }
            
            isSearching = false
        }
    }

    /**
     * Adiciona uma música da busca diretamente
     */
    fun addSongFromSearch(songItem: AuddSongItem) {
        val title = songItem.title ?: "Sem título"
        val artist = songItem.artist ?: "Artista desconhecido"
        val lyrics = songItem.lyrics?.fullLyrics 
            ?: songItem.lyrics?.lyrics 
            ?: "Letra não disponível"
        
        addSong(title, artist, lyrics)
    }

    /**
     * Busca letra específica por artista e título
     */
    fun fetchLyrics(artist: String, title: String, onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            isLoadingLyrics = true
            lyricError = null
            
            lyricsRepository.findLyrics(artist, title)
                .onSuccess { (realTitle, lyrics) ->
                    onSuccess(lyrics)
                }
                .onFailure { error ->
                    lyricError = error.message ?: "Erro ao buscar letra"
                }
            
            isLoadingLyrics = false
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

    fun clearSearchError() {
        searchError = null
    }

    fun clearLyricError() {
        lyricError = null
    }

    fun openDetail(id: String) {
        selectedSongId = id
        currentScreen = Screen.Detail
    }

    fun navigateToAdd() { currentScreen = Screen.Add }
    fun navigateToSearch() { currentScreen = Screen.Search }
    fun navigateToEdit(id: String) { selectedSongId = id; currentScreen = Screen.Edit }
    fun navigateToList() { 
        currentScreen = Screen.List
        selectedSongId = null
        searchResults = emptyList()
        searchError = null
    }
}