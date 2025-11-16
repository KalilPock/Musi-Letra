package com.example.musiletra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.musiletra.ui.screens.AddEditSongScreen
import com.example.musiletra.ui.screens.OnlineSearchScreen
import com.example.musiletra.ui.screens.PlaylistDetailsScreen
import com.example.musiletra.ui.screens.PlaylistInfoScreen
import com.example.musiletra.ui.screens.PlaylistsScreen
import com.example.musiletra.ui.viewmodels.PlaylistsViewModel
import com.example.musiletra.ui.viewmodels.SongViewModel

// Enum para gerenciar as rotas de forma segura
enum class Routes(val route: String) {
    PLAYLISTS("playlists"), PLAYLIST_DETAILS("playlists/{playlistId}"), PLAYLIST_INFO("playlists/info/{playlistId}"), SONG_DETAILS(
        "song/{songId}"
    ),
    EDIT_SONG("edit_song/{songId}"), ADD_SONG("add_song"), ONLINE_SEARCH("online_search")
}

val playlistsViewModel = PlaylistsViewModel()
val songViewModel = SongViewModel()

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route
                Scaffold(
                    topBar = {
                        MusiLetraTopAppBar(
                            currentRoute = currentRoute,
                            canNavigateBack = navController.previousBackStackEntry != null,
                            navigateUp = { navController.navigateUp() },
                            navController = navController
                        )
                    }, modifier = Modifier.padding(10.dp)
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.PLAYLISTS.route,
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        composable(Routes.PLAYLISTS.route) {
                            PlaylistsScreen(
                                navController = navController, viewModel = playlistsViewModel
                            )
                        }
                        composable(
                            route = Routes.PLAYLIST_DETAILS.route,
                            arguments = listOf(navArgument("playlistId") { type = NavType.IntType })
                        ) { navBackStackEntry ->
                            val playlistId = navBackStackEntry.arguments?.getInt("playlistId")
                            if (playlistId != null) {
                                PlaylistDetailsScreen(
                                    playlistsViewModel = playlistsViewModel,
                                    songViewModel = songViewModel,
                                    playlistId = playlistId,
                                    onOpenSong = { songId ->
                                        navController.navigate(
                                            Routes.SONG_DETAILS.route.replace("{songId}", songId)
                                        )
                                    },
                                    onEditSong = { songId ->
                                        navController.navigate(
                                            Routes.EDIT_SONG.route.replace("{songId}", songId)
                                        )
                                    },
                                    onDeleteSong = { songId -> songViewModel.deleteSong(songId) })
                            } else {
                                Text("Error: Playlist ID not found.")
                            }
                        }
                        composable(
                            route = Routes.PLAYLIST_INFO.route,
                            arguments = listOf(navArgument("playlistId") {
                                type = NavType.IntType
                            })
                        ) { navBackStackEntry ->
                            val playlistId = navBackStackEntry.arguments?.getInt("playlistId")
                            if (playlistId != null) {
                                PlaylistInfoScreen(
                                    navController = navController,
                                    idPlaylist = playlistId,
                                    viewModel = playlistsViewModel
                                )
                            } else {
                                Text("Error: Playlist ID not found.")
                            }
                        }
                        composable(
                            route = Routes.SONG_DETAILS.route,
                            arguments = listOf(navArgument("songId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val songId = backStackEntry.arguments?.getString("songId")
                            AddEditSongScreen(
                                songViewModel = songViewModel,
                                existingSongId = songId,
                                onSave = { navController.popBackStack() },
                                onCancel = { navController.popBackStack() })
                        }
                        composable(
                            route = Routes.EDIT_SONG.route,
                            arguments = listOf(navArgument("songId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val songId = backStackEntry.arguments?.getString("songId")
                            AddEditSongScreen(
                                songViewModel = songViewModel,
                                existingSongId = songId,
                                onSave = { navController.popBackStack() },
                                onCancel = { navController.popBackStack() })
                        }
                        composable(Routes.ADD_SONG.route) {
                            AddEditSongScreen(
                                songViewModel = songViewModel,
                                onSave = { navController.popBackStack() },
                                onCancel = { navController.popBackStack() })
                        }
                        composable(Routes.ONLINE_SEARCH.route) {
                            OnlineSearchScreen(songViewModel = songViewModel)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusiLetraTopAppBar(
    currentRoute: String?,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            val title = when (currentRoute) {
                Routes.PLAYLISTS.route -> "Minhas Playlists"
                Routes.PLAYLIST_DETAILS.route -> "Detalhes da Playlist"
                Routes.PLAYLIST_INFO.route -> "Informações da Playlist"
                Routes.SONG_DETAILS.route -> "Detalhes da Música"
                Routes.EDIT_SONG.route -> "Editar Música"
                Routes.ADD_SONG.route -> "Adicionar Música"
                Routes.ONLINE_SEARCH.route -> "Buscar Online"
                else -> "Musiletra"
            }
            Text(title)
        },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar"
                    )
                }
            }
        },
        actions = {
            if (currentRoute == Routes.PLAYLISTS.route) {
                IconButton(onClick = { // TODO: Navegar para a tela de adicionar playlist

                }) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar playlist")
                }
                IconButton(onClick = { navController.navigate(Routes.ONLINE_SEARCH.route) }) {
                    Icon(Icons.Default.Search, contentDescription = "Pesquisar online")
                }
            }
        }
    )
}
