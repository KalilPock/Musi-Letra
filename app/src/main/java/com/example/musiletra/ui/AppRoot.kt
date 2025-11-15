package com.example.musiletra.ui

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musiletra.ui.screens.AddEditSongScreen
import com.example.musiletra.ui.screens.OnlineSearchScreen
import com.example.musiletra.ui.screens.PlaylistDetailScreen
import com.example.musiletra.ui.screens.PlaylistListScreen
import com.example.musiletra.ui.screens.SongDetailScreen
import com.example.musiletra.ui.screens.SongListScreen

@Composable
fun AppRoot(
    songViewModel: SongViewModel,
    playlistViewModel: PlaylistViewModel
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val songs = songViewModel.songs
    val playlists = playlistViewModel.playlists

    NavHost(navController = navController, startDestination = "songList") {
        composable("songList") {
            SongListScreen(
                songs = songs,
                onAdd = { navController.navigate("addSong") },
                onOpen = { id -> navController.navigate("songDetail/$id") },
                onEdit = { id -> navController.navigate("editSong/$id") },
                onDelete = { id -> songViewModel.deleteSong(id) },
                onGoToSearch = { navController.navigate("onlineSearch") },
                onGoToPlaylists = { navController.navigate("playlistList") }
            )
        }
        composable("addSong") {
            AddEditSongScreen(
                onSave = { title, artist, lyrics ->
                    songViewModel.addSong(title, artist, lyrics)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }
        composable("editSong/{songId}") { backStackEntry ->
            val songId = backStackEntry.arguments?.getString("songId") ?: return@composable
            val song = songs.firstOrNull { it.id == songId } ?: return@composable
            AddEditSongScreen(
                existing = song,
                onSave = { title, artist, lyrics ->
                    songViewModel.editSong(songId, title, artist, lyrics)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }
        composable("songDetail/{songId}") { backStackEntry ->
            val songId = backStackEntry.arguments?.getString("songId") ?: return@composable
            val song = songs.firstOrNull { it.id == songId } ?: return@composable
            SongDetailScreen(
                song = song,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate("editSong/$songId") },
                onDelete = { id ->
                    songViewModel.deleteSong(id)
                    navController.popBackStack()
                },
                onShare = {
                    // Criar Intent de compartilhamento (ACTION_SEND)
                    val shareText = buildString {
                        append("🎵 ${song.title}\n")
                        if (song.artist.isNotBlank()) {
                            append("🎤 ${song.artist}\n")
                        }
                        append("\n${song.lyrics}")
                    }

                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareText)
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, "Compartilhar letra via")
                    context.startActivity(shareIntent)
                }
            )
        }
        composable("onlineSearch") {
            OnlineSearchScreen(
                songs = songViewModel.onlineSearchResults,
                onSearch = { query -> songViewModel.searchOnline(query) },
                onAdd = { title, artist, lyrics ->
                    songViewModel.addSong(title, artist, lyrics)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable("playlistList") {
            PlaylistListScreen(
                playlists = playlists,
                onCreatePlaylist = { name, description ->
                    playlistViewModel.createPlaylist(name, description)
                },
                onOpenPlaylist = { id -> navController.navigate("playlistDetail/$id") },
                onDeletePlaylist = { id -> playlistViewModel.deletePlaylist(id) },
                onBack = { navController.popBackStack() }
            )
        }
        composable("playlistDetail/{playlistId}") { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getString("playlistId") ?: return@composable
            PlaylistDetailScreen(
                playlistId = playlistId,
                playlistViewModel = playlistViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
