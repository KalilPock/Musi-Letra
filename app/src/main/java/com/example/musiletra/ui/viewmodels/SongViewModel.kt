package com.example.musiletra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.musiletra.data.AudDSong
import com.example.musiletra.data.RetrofitClient
import com.example.musiletra.data.database.MusicaDao
import com.example.musiletra.data.database.MusicaSalva
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// REMOVEMOS A LISTA 'static_songs' DAQUI
// ELA NÃO É MAIS NECESSÁRIA

// --- MUDANÇA 1: O ViewModel agora RECEBE o DAO no construtor ---
// Isso se chama "Injeção de Dependência".
class SongViewModel(private val musicaDao: MusicaDao) : ViewModel() {

    // --- MUDANÇA 2: State para o ID do usuário ---
    // Precisamos saber quem está logado.
    // 'null' significa que é um "convidado" (pulou o login)
    private val _currentUserId = MutableStateFlow<Int?>(null)

    // --- MUDANÇA 3: O 'songs' agora é um 'Flow' que reage ao usuário ---
    // Ele busca automaticamente as músicas do usuário logado (ou do convidado)
    @OptIn(ExperimentalCoroutinesApi::class)
    val songs: StateFlow<List<MusicaSalva>> = _currentUserId.flatMapLatest { userId ->
        // Usamos a consulta do DAO que já trata 'userId' nulo ou não
        musicaDao.getPlaylistDoUsuario(userId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), // Mantém o flow ativo por 5s
        initialValue = emptyList() // Começa com uma lista vazia
    )

    // State for the online search results (Isso não muda)
    var onlineSearchResults by mutableStateOf<List<AudDSong>>(emptyList())
        private set

    // --- MUDANÇA 4: Função para a Activity/UI definir o usuário ---
    /**
     * Chamado pela UI (ex: MainActivity) após o login ou "Pular".
     * @param usuarioId O ID do usuário logado, ou 'null' se for convidado.
     */
    fun setUsuario(usuarioId: Int?) {
        _currentUserId.value = usuarioId
    }


    // --- MUDANÇA 5: CRUD agora usa o DAO e 'MusicaSalva' ---

    /**
     * Adiciona uma música (da tela de busca ou manual) AO BANCO DE DADOS.
     */
    fun addSong(title: String, artist: String, lyrics: String) {
        viewModelScope.launch {
            val novaMusica = MusicaSalva(
                // O 'id' é gerado automaticamente pelo Room
                usuarioId = _currentUserId.value, // Associa ao usuário logado!
                titulo = title,
                artista = artist,
                letra = lyrics,
                apiSongId = null // Você pode salvar o ID da API aqui se tiver
            )
            // Salva no banco
            musicaDao.salvarMusica(novaMusica)
        }
    }

    /**
     * Edita uma música existente NO BANCO DE DADOS.
     * Nota: O 'songId' agora é um Int (do Room), não mais uma String (do UUID).
     */
    fun editSong(songId: Int, title: String, artist: String, lyrics: String) {
        viewModelScope.launch {
            val musicaAtualizada = MusicaSalva(
                id = songId, // ID da música que estamos editando
                usuarioId = _currentUserId.value, // Mantém a associação
                titulo = title,
                artista = artist,
                letra = lyrics,
                apiSongId = null
            )
            // Atualiza no banco
            musicaDao.atualizarMusica(musicaAtualizada)
        }
    }

    /**
     * Deleta uma música DO BANCO DE DADOS.
     * É mais fácil e seguro passar o objeto inteiro para deletar.
     */
    fun deleteSong(musica: MusicaSalva) {
        viewModelScope.launch {
            // Deleta do banco
            musicaDao.removerMusica(musica)
        }
    }

    // --- A Busca Online continua igual ---
    // (Ela não depende do banco de dados local)
    fun searchOnline(query: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.findByLyrics(query)
                if (response.status == "success") {
                    onlineSearchResults = response.result ?: emptyList()
                } else {
                    val errorMessage = response.error?.message ?: "Unknown error"
                    val errorCode = response.error?.code ?: "N/A"
                    println("AudD API Error ($errorCode): $errorMessage")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onlineSearchResults = emptyList()
            }
        }
    }
}


// --- MUDANÇA 6: Factory para criar o ViewModel ---
/**
 * Como o SongViewModel agora precisa de um 'MusicaDao' no construtor,
 * não podemos mais usá-lo com 'viewModel()' diretamente.
 * Precisamos desta "Fábrica" para dizer ao Android como criá-lo.
 */
class SongViewModelFactory(
    private val musicaDao: MusicaDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SongViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SongViewModel(musicaDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}