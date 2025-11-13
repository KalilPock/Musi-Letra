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
import com.example.musiletra.data.database.AppDatabase // Importe o Banco
import com.example.musiletra.data.database.MusicaSalva
import androidx.activity.viewModels
// Importe sua nova tela de Login (você precisará criar este arquivo)
// import com.example.musiletra.ui.screens.LoginScreen
import com.example.musiletra.ui.screens.AddEditSongScreen
import com.example.musiletra.ui.screens.OnlineSearchScreen
import com.example.musiletra.ui.screens.PlaylistDetailsScreen
import com.example.musiletra.ui.screens.PlaylistInfoScreen
import com.example.musiletra.ui.screens.PlaylistsScreen
import com.example.musiletra.ui.viewmodels.PlaylistsViewModel
import com.example.musiletra.ui.viewmodels.SongViewModel
import com.example.musiletra.ui.viewmodels.SongViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    // --- MUDANÇA 1: Inicializar o Banco e os ViewModels ---
    
    // 1. Crie a instância do banco de dados
    private val database by lazy { AppDatabase.getDatabase(this) }

    // 2. Crie a factory que usa o DAO do banco
    private val songViewModelFactory by lazy { SongViewModelFactory(database.musicaDao()) }

    // 3. Crie o SongViewModel usando a factory
    private val songViewModel: SongViewModel by viewModels { songViewModelFactory }
    
    // 4. Crie o PlaylistsViewModel (ele não precisa de factory)
    private val playlistsViewModel: PlaylistsViewModel by viewModels()
    // --- Fim da Mudança 1 ---

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
                        // --- MUDANÇA 2: Mudar a rota inicial ---
                        startDestination = "login", // Começa na tela de login
                        modifier = Modifier.padding(innerPadding),
                    ) {

                        // --- MUDANÇA 3: Adicionar a nova rota de Login ---
                        composable("login") {
                            /* // Descomente isso quando criar sua LoginScreen
                            
                            LoginScreen(
                                navController = navController,
                                // (Você precisará do UsuarioDao, talvez em um UsuarioViewModel)
                                onLoginSuccess = { usuario ->
                                    // Seta o usuário no ViewModel!
                                    songViewModel.setUsuario(usuario.id) 
                                    // Navega para as playlists
                                    navController.navigate("playlists") {
                                        popUpTo("login") { inclusive = true } // Limpa a pilha de login
                                    }
                                },
                                onSkip = {
                                    // Seta o usuário como "convidado" (null)
                                    songViewModel.setUsuario(null) 
                                    // Navega para as playlists
                                    navController.navigate("playlists") {
                                        popUpTo("login") { inclusive = true } // Limpa a pilha de login
                                    }
                                }
                            )
                            */
                            // Placeholder_Login: Remova isso depois
                            Text("Placeholder: Crie sua LoginScreen.kt aqui.")
                        }
                        
                        // --- Fim da Mudança 3 ---
                        
                        composable("playlists") {
                            PlaylistsScreen(
                                navController = navController, 
                                viewModel = playlistsViewModel
                            )
                        }
                        composable(
                            route = "playlists/{playlistId}",
                            arguments = listOf(navArgument("playlistId") { type = NavType.IntType })
                        ) { navBackStackEntry ->
                            val playlistId = navBackStackEntry.arguments?.getInt("playlistId")
                            if (playlistId != null) {
                                PlaylistDetailsScreen(
                                    playlistsViewModel = playlistsViewModel,
                                    songViewModel = songViewModel, // Passando o VM correto
                                    playlistId = playlistId,
                                    onOpenSong = { songId -> navController.navigate("song/$songId") },
                                    onEditSong = { songId -> navController.navigate("edit_song/$songId") },
                                    
                                    // --- MUDANÇA 4: Corrigir o onDelete ---
                                    // Assumindo que sua PlaylistDetailsScreen foi atualizada
                                    // para passar o objeto MusicaSalva inteiro.
                                    onDeleteSong = { musica ->
                                        songViewModel.deleteSong(musica)
                                    }
                                )
                            } else {
                                Text("Error: Playlist ID not found.")
                            }
                        }
                        composable(
                            route = "playlists/info/{playlistId}",
                            // ... (sem alteração aqui)
                        ) { 
                            // ...
                        }

                        // --- MUDANÇA 5: Corrigir tipo do songId (String -> Int) ---
                        composable(
                            route = "song/{songId}",
                            arguments = listOf(navArgument("songId") { Route = NavType.IntType }) // MUDADO
                        ) { backStackEntry ->
                            // getInt() em vez de getString()
                            val songId = backStackEntry.arguments?.getInt("songId") // MUDADO
                            
                            AddEditSongScreen(
                                songViewModel = songViewModel,
                                existingSongId = songId, // Passando Int?
                                onSave = { navController.popBackStack() },
                                onCancel = { navController.popBackStack() }
                            )
                        }
                        composable(
                            route = "edit_song/{songId}",
                            arguments = listOf(navArgument("songId") { type = NavType.IntType }) // MUDADO
                        ) { backStackEntry ->
                            // getInt() em vez de getString()
                            val songId = backStackEntry.arguments?.getInt("songId") // MUDADO
                            AddEditSongScreen(
                                songViewModel = songViewModel,
                                existingSongId = songId, // Passando Int?
                                onSave = { navController.popBackStack() },
                                onCancel = { navController.popBackStack() }
                            )
                        }
                        // --- Fim da Mudança 5 ---

                        composable("add_song") {
                            AddEditSongScreen(
                                songViewModel = songViewModel,
                                onSave = { navController.popBackStack() },
                                onCancel = { navController.popBackStack() }
                            )
                        }
                        composable("online_search") {
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
                "playlists" -> "Minhas Playlists"
                "playlists/{playlistId}" -> "Detalhes da Playlist"
                "playlists/info/{playlistId}" -> "Informações da Playlist"
                "song/{songId}" -> "Detalhes da Música"
                "edit_song/{songId}" -> "Editar Música"
                "add_song" -> "Adicionar Música"
                "online_search" -> "Buscar Online"
                else -> "Musiletra"
            }
            Text(title)
        },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar"
                    )
                }
            }
        },
        actions = {
            if (currentRoute == "playlists") {
                IconButton(onClick = { navController.navigate("add_song") }) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar música")
                }
                IconButton(onClick = { navController.navigate("online_search") }) {
                    Icon(Icons.Default.Search, contentDescription = "Pesquisar online")
                }
            }
        }
    )
}