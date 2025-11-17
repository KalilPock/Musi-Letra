package com.example.musiletra.ui

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.musiletra.ui.screens.AddEditSongScreen
import com.example.musiletra.ui.screens.OnlineSearchScreen
import com.example.musiletra.ui.screens.PlaylistDetailScreen
import com.example.musiletra.ui.screens.PlaylistListScreen
import com.example.musiletra.ui.screens.SongDetailScreen
import com.example.musiletra.ui.screens.SongListScreen
import com.example.musiletra.ui.viewmodels.PlaylistViewModel
import com.example.musiletra.ui.viewmodels.SongViewModel

enum class Routes(val route: String) {
    SONG_LIST("songList"),
    ADD_SONG("addSong"),
    EDIT_SONG("editSong/{songId}"),
    SONG_DETAILS("songDetail/{songId}"),
    ONLINE_SEARCH("onlineSearch"),
    PLAYLISTS("playlistList"),
    PLAYLIST_DETAILS("playlistDetail/{playlistId}")
}

@Composable
fun AppRoot(
    songViewModel: SongViewModel,
    playlistViewModel: PlaylistViewModel
) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val songs by songViewModel.songs.collectAsState()

    NavHost(navController = navController, startDestination = Routes.SONG_LIST.route) {
        composable(Routes.SONG_LIST.route) {
            SongListScreen(
                songs = songs,
                onAdd = { navController.navigate(Routes.ADD_SONG.route) },
                onOpen = { id -> navController.navigate(Routes.SONG_DETAILS.route.replace("{songId}", id)) },
                onEdit = { id -> navController.navigate(Routes.EDIT_SONG.route.replace("{songId}", id)) },
                onDelete = { id -> songViewModel.deleteSong(id) },
                onGoToSearch = { navController.navigate(Routes.ONLINE_SEARCH.route) },
                onGoToPlaylists = { navController.navigate(Routes.PLAYLISTS.route) }
            )
        }
        composable(Routes.ADD_SONG.route) {
            AddEditSongScreen(
                onSave = { title, artist, lyrics ->
                    songViewModel.addSong(title, artist, lyrics)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }
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
        composable(
            route = Routes.SONG_DETAILS.route,
            arguments = listOf(navArgument("songId") { type = NavType.StringType })
        ) { backStackEntry ->
            val songId = backStackEntry.arguments?.getString("songId") ?: return@composable
            val song = songs.firstOrNull { it.id == songId } ?: return@composable
            SongDetailScreen(
                song = song,
                onBack = { navController.popBackStack() },
                onEdit = { navController.navigate(Routes.EDIT_SONG.route.replace("{songId}", songId)) },
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
        composable(Routes.ONLINE_SEARCH.route) {
            val onlineSongs by songViewModel.onlineSearchResults.collectAsState()
            OnlineSearchScreen(
                songs = onlineSongs,
                onSearch = { query -> songViewModel.searchOnline(query) },
                onAdd = { title, artist, lyrics ->
                    songViewModel.addSong(title, artist, lyrics)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.PLAYLISTS.route) {
            PlaylistListScreen(
                playlistViewModel = playlistViewModel,
                onOpenPlaylist = { id -> navController.navigate(Routes.PLAYLIST_DETAILS.route.replace("{playlistId}", id.toString())) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.PLAYLIST_DETAILS.route,
            arguments = listOf(navArgument("playlistId") { type = NavType.IntType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getInt("playlistId") ?: return@composable
            PlaylistDetailScreen(
                playlistId = playlistId,
                playlistViewModel = playlistViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
