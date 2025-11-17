package com.example.musiletra.ui

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.musiletra.ui.screens.*

@Composable
fun AppRoot(
    songViewModel: SongViewModel,
    playlistViewModel: PlaylistViewModel,
    usuarioViewModel: UsuarioViewModel
) {
    val navController = rememberNavController()
    val songs by songViewModel.songs.collectAsStateWithLifecycle()
    val playlists by playlistViewModel.playlists.collectAsStateWithLifecycle()

    // Determina tela inicial baseado no estado de login
    val startDestination = if (usuarioViewModel.usuarioLogado == null) "login" else "songList"

    NavHost(navController = navController, startDestination = startDestination) {
        // === LOGIN ROUTE ===
        composable("login") {
            LoginScreen(
                usuarioViewModel = usuarioViewModel,
                onLoginSuccess = {
                    navController.navigate("songList") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onSkip = {
                    navController.navigate("songList") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        // === SONGS ROUTES ===
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

        composable(
            route = "editSong/{songId}",
            arguments = listOf(navArgument("songId") { type = NavType.IntType })
        ) { backStackEntry ->
            val songId = backStackEntry.arguments?.getInt("songId") ?: return@composable
            val song = songs.firstOrNull { it.id == songId }

            if (song != null) {
                AddEditSongScreen(
                    existingSong = song,
                    onSave = { title, artist, lyrics ->
                        songViewModel.editSong(songId, title, artist, lyrics)
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
        }

        composable(
            route = "songDetail/{songId}",
            arguments = listOf(navArgument("songId") { type = NavType.IntType })
        ) { backStackEntry ->
            val songId = backStackEntry.arguments?.getInt("songId") ?: return@composable
            val song = songs.firstOrNull { it.id == songId }

            if (song != null) {
                SongDetailScreen(
                    song = song,
                    onBack = { navController.popBackStack() },
                    onEdit = { navController.navigate("editSong/$songId") },
                    onDelete = { id ->
                        songViewModel.deleteSong(id)
                        navController.popBackStack()
                    }
                )
            }
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

        // === PLAYLIST ROUTES ===
        composable("playlistList") {
            PlaylistListScreen(
                playlists = playlists,
                onAdd = { navController.navigate("addPlaylist") },
                onOpen = { id -> navController.navigate("playlistDetail/$id") },
                onEdit = { id -> navController.navigate("editPlaylist/$id") },
                onDelete = { id -> playlistViewModel.deletePlaylist(id) },
                onBack = { navController.popBackStack() }
            )
        }

        composable("addPlaylist") {
            AddEditPlaylistScreen(
                onSave = { nome, descricao ->
                    playlistViewModel.addPlaylist(nome, descricao)
                    navController.popBackStack()
                },
                onCancel = { navController.popBackStack() }
            )
        }

        composable(
            route = "editPlaylist/{playlistId}",
            arguments = listOf(navArgument("playlistId") { type = NavType.IntType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getInt("playlistId") ?: return@composable
            val playlist = playlists.firstOrNull { it.id == playlistId }

            if (playlist != null) {
                AddEditPlaylistScreen(
                    existingPlaylist = playlist,
                    onSave = { nome, descricao ->
                        playlistViewModel.editPlaylist(playlistId, nome, descricao)
                        navController.popBackStack()
                    },
                    onCancel = { navController.popBackStack() }
                )
            }
        }

        composable(
            route = "playlistDetail/{playlistId}",
            arguments = listOf(navArgument("playlistId") { type = NavType.IntType })
        ) { backStackEntry ->
            val playlistId = backStackEntry.arguments?.getInt("playlistId") ?: return@composable
            val playlist = playlists.firstOrNull { it.id == playlistId }

            if (playlist != null) {
                PlaylistDetailScreen(
                    playlist = playlist,
                    allSongs = songs,
                    onBack = { navController.popBackStack() },
                    onEdit = { navController.navigate("editPlaylist/$playlistId") },
                    onDelete = { id ->
                        playlistViewModel.deletePlaylist(id)
                        navController.popBackStack()
                    },
                    onSongClick = { songId -> navController.navigate("songDetail/$songId") },
                    onAddSong = { musicaId ->
                        playlistViewModel.addMusicaToPlaylist(playlistId, musicaId)
                    },
                    onRemoveSong = { musicaId ->
                        playlistViewModel.removeMusicaFromPlaylist(playlistId, musicaId)
                    }
                )
            }
        }
    }
}
