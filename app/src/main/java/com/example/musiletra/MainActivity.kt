package com.example.musiletra

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.musiletra.ui.screens.OnlineSearchScreen
import com.example.musiletra.ui.screens.PlaylistDetailsScreen
import com.example.musiletra.ui.screens.PlaylistsScreen
import com.example.musiletra.ui.screens.SongListScreen

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route

                Scaffold(topBar = {
                    MusiLetraTopAppBar(
                        currentRoute = currentRoute,
                        canNavigateBack = navController.previousBackStackEntry != null,
                        navigateUp = { navController.navigateUp() })
                }

                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "playlists",
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        composable("playlists") {
                            PlaylistsScreen(navController = navController)
                        }
                        composable(
                            route = "playlist_details/{playlistId}",
                            arguments = listOf(navArgument("playlistId") { type = NavType.IntType })
                        ) { navBackStackEntry ->
                            val playlistId = navBackStackEntry.arguments?.getInt("playlistId")
                            if (playlistId != null) {

                                PlaylistDetailsScreen(
                                    playlistId = playlistId,
                                    onOpenSong = { songId -> navController.navigate("song_detail/$songId") },
                                    onEditSong = { songId -> /* TODO: navController.navigate("edit_song/$songId") */ },
                                    onDeleteSong = { songId -> /* TODO: viewModel.deleteSong(songId) */ })
                            } else {
                                Text("Error: Playlist ID not found.")
                            }
                        }
                        composable("song_list") {
                            SongListScreen(/* Pass necessary parameters */)
                        }
                        composable(
                            route = "song_detail/{songId}",
                            arguments = listOf(navArgument("songId") {
                                type = NavType.StringType
                            }) // Changed to String as per your model
                        ) {
                            OnlineSearchScreen()
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
    modifier: Modifier = Modifier
) {
    TopAppBar(title = {
        val title = when (currentRoute) {
            "playlists" -> "Minhas Playlists"
            "playlist_details/{playlistId}" -> "Detalhes da Playlist"
            else -> "Musiletra"
        }
        Text(title)
    }, modifier = modifier, navigationIcon = {
        if (canNavigateBack) {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar"
                )
            }
        }
    }, actions = {
        if (currentRoute == "playlists") {
            IconButton(onClick = { /* TODO: Handle Search Navigation */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }
    })
}
