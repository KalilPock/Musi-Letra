package com.example.musiletra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.musiletra.ui.AppRoot
import com.example.musiletra.ui.PlaylistViewModel
import com.example.musiletra.ui.SongViewModel
import com.example.musiletra.ui.theme.MusiLetraTheme

class MainActivity : ComponentActivity() {
    private val songViewModel: SongViewModel by viewModels()
    private val playlistViewModel: PlaylistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusiLetraTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppRoot(
                        songViewModel = songViewModel,
                        playlistViewModel = playlistViewModel
                    )
                }
            }
        }
    }
}
