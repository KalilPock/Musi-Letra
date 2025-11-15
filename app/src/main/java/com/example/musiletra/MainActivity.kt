package com.example.musiletra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.musiletra.data.AppDatabase
import com.example.musiletra.data.PlaylistRepository
import com.example.musiletra.data.SongRepository
import com.example.musiletra.ui.AppRoot
import com.example.musiletra.ui.PlaylistViewModel
import com.example.musiletra.ui.PlaylistViewModelFactory
import com.example.musiletra.ui.SongViewModel
import com.example.musiletra.ui.SongViewModelFactory

class MainActivity : ComponentActivity() {

    // Initialize database and repositories
    private val database by lazy { AppDatabase.getDatabase(this) }
    private val songRepository by lazy { SongRepository(database.songDao()) }
    private val playlistRepository by lazy { PlaylistRepository(database.playlistDao()) }

    // Initialize ViewModels with factories
    private val songViewModel: SongViewModel by viewModels {
        SongViewModelFactory(songRepository)
    }

    private val playlistViewModel: PlaylistViewModel by viewModels {
        PlaylistViewModelFactory(playlistRepository, songRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppRoot(songViewModel, playlistViewModel)
                }
            }
        }
    }
}
