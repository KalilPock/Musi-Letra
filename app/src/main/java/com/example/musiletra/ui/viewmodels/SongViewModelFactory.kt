package com.example.musiletra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musiletra.data.database.MusicaDao

/**
 * Factory para criar o SongViewModel, já que ele agora precisa
 * de um 'MusicaDao' no seu construtor.
 */
class SongViewModelFactory(
    private val musicaDao: MusicaDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Verifica se a classe que o Android está a pedir é o nosso SongViewModel
        if (modelClass.isAssignableFrom(SongViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SongViewModel(musicaDao) as T
        }
        // Se não for, lança um erro
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}