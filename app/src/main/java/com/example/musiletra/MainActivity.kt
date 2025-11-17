package com.example.musiletra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.musiletra.data.PlaylistRepository
import com.example.musiletra.data.SongRepository
import com.example.musiletra.data.database.AppDatabase
import com.example.musiletra.ui.AppRoot
import com.example.musiletra.ui.viewmodels.PlaylistViewModel
import com.example.musiletra.ui.viewmodels.SongViewModel
import com.example.musiletra.ui.viewmodels.ViewModelFactory

class MainActivity : ComponentActivity() {

    // Initialize database and repositories
    private val database by lazy { AppDatabase.getDatabase(this) }
    private val songRepository by lazy { SongRepository(database.songDao()) }
    private val playlistRepository by lazy { PlaylistRepository(database.playlistDao()) }

    // Unified ViewModelFactory
    private val viewModelFactory by lazy { 
        ViewModelFactory(songRepository, playlistRepository) 
    }

    // Initialize ViewModels with the unified factory
    private val songViewModel: SongViewModel by viewModels { viewModelFactory }
    private val playlistViewModel: PlaylistViewModel by viewModels { viewModelFactory }

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
