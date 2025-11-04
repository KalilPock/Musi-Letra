package com.example.musiletra.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.musiletra.ui.screens.AddEditSongScreen
import com.example.musiletra.ui.screens.OnlineSearchScreen
import com.example.musiletra.ui.screens.SongDetailScreen
import com.example.musiletra.ui.screens.SongListScreen

@Composable
fun AppRoot(viewModel: SongViewModel) {
    val navController = rememberNavController()
    val songs = viewModel.songs

    NavHost(navController = navController, startDestination = "songList") {
        composable("songList") {
            SongListScreen(
                songs = songs,
                onAdd = { navController.navigate("addSong") },
                onOpen = { id -> navController.navigate("songDetail/$id") },
                onEdit = { id -> navController.navigate("editSong/$id") },
                onDelete = { id -> viewModel.deleteSong(id) },
                onGoToSearch = { navController.navigate("onlineSearch") }
            )
        }
        composable("addSong") {
            AddEditSongScreen(
                onSave = { title, artist, lyrics ->
                    viewModel.addSong(title, artist, lyrics)
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
                    viewModel.editSong(songId, title, artist, lyrics)
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
                    viewModel.deleteSong(id)
                    navController.popBackStack()
                }
            )
        }
        composable("onlineSearch") {
            OnlineSearchScreen(
                songs = viewModel.onlineSearchResults,
                onSearch = { query -> viewModel.searchOnline(query) },
                onAdd = { title, artist, lyrics ->
                    viewModel.addSong(title, artist, lyrics)
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
