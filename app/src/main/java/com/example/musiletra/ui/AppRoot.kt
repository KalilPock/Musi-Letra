package com.example.musiletra.ui

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.musiletra.ui.screens.AddEditSongScreen
import com.example.musiletra.ui.screens.OnlineSearchScreen
import com.example.musiletra.ui.screens.PlaylistDetailScreen
import com.example.musiletra.ui.screens.PlaylistInfoScreen
import com.example.musiletra.ui.screens.PlaylistListScreen
import com.example.musiletra.ui.screens.SongDetailScreen
import com.example.musiletra.ui.screens.SongListScreen
import com.example.musiletra.ui.viewmodels.PlaylistViewModel
import com.example.musiletra.ui.viewmodels.SongViewModel

@Composable
fun AppRoot(
    songViewModel: SongViewModel,
    playlistViewModel: PlaylistViewModel
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val songs = songViewModel.songs
    val playlists = playlistViewModel.playlists

    NavHost(navController = navController, startDestination = Routes.SONG_LIST.route) {
        // Song List Screen
        composable(Routes.SONG_LIST.route) {
            SongListScreen(
                songs = songs,
                onAdd = { navController.navigate(Routes.ADD_SONG.route) },
                onOpen = { id -> navController.navigate(Routes.songDetails(id)) },
                onEdit = { id -> navController.navigate(Routes.editSong(id)) },
                onDelete = { id -> songViewModel.deleteSong(id) },
                onGoToSearch = { navController.navigate(Routes.ONLINE_SEARCH.route) },
                onGoToPlaylists = { navController.navigate(Routes.PLAYLISTS.route) }
            )
        }

        // Add Song Screen
        composable(Routes.ADD_SONG.route) {
            AddEditSongScreen(
                onSave = { title, artist, lyrics ->
                    songViewModel.addSong(title, artist, lyrics)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }

        // Edit Song Screen
        composable(
            route = Routes.EDIT_SONG.route,
            arguments = listOf(navArgument("songId") { type = NavType.StringType })
        ) { backStackEntry ->
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

        // Song Details Screen
        composable(
            route = Routes.SONG_DETAILS.route,
            arguments = listOf(navArgument("songId") { type = NavType.StringType })
        ) { backStackEntry ->
            val songId = backStackEntry.arguments?.getString("songId") ?: return@composable
            val song = songs.firstOrNull { it.id == songId } ?: return@composable
            SongDetailScreen(
                song = song,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(Routes.editSong(songId)) },
                onDelete = { id ->
                    songViewModel.deleteSong(id)
                    navController.popBackStack()
                },
                onShare = {
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

        // Online Search Screen
        composable(Routes.ONLINE_SEARCH.route) {
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

        // Playlists Screen
        composable(Routes.PLAYLISTS.route) {
            PlaylistListScreen(
                playlists = playlists,
                onCreatePlaylist = { name, description ->
                    playlistViewModel.createPlaylist(name, description)
                },
                onOpenPlaylist = { id -> navController.navigate(Routes.playlistDetails(id)) },
                onPlaylistInfo = { id -> navController.navigate(Routes.playlistInfo(id)) },
                onDeletePlaylist = { id -> playlistViewModel.deletePlaylist(id) },
                onBack = { navController.popBackStack() }
            )
        }

        // Playlist Details Screen
        composable(
            route = Routes.PLAYLIST_DETAILS.route,
            arguments = listOf(navArgument("playlistId") { type = NavType.IntType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getInt("playlistId") ?: return@composable
            PlaylistDetailScreen(
                playlistId = playlistId,
                playlistViewModel = playlistViewModel,
                onBack = { navController.popBackStack() },
                onInfo = { navController.navigate(Routes.playlistInfo(playlistId)) }
            )
        }

        // Playlist Info Screen
        composable(
            route = Routes.PLAYLIST_INFO.route,
            arguments = listOf(navArgument("playlistId") { type = NavType.IntType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getInt("playlistId") ?: return@composable
            PlaylistInfoScreen(
                playlistId = playlistId,
                playlistViewModel = playlistViewModel,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(Routes.playlistDetails(playlistId)) }
            )
        }
    }
}
