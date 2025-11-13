package com.example.musiletra.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.musiletra.ui.viewmodels.UsuarioViewModel
import com.example.musiletra.ui.viewmodels.UsuarioViewModelFactory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    usuarioViewModel: UsuarioViewModel,
    onLoginSuccess: (Int) -> Unit, // Recebe o ID do usuário
    onSkip: () -> Unit // Chamado ao pular
) {
    // Coleta o estado do ViewModel (nome, email, senha, loading, erro)
    val uiState = usuarioViewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text("MusiLetra", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(32.dp))

        // Campo de Nome (para registro)
        OutlinedTextField(
            value = uiState.nome,
            onValueChange = { usuarioViewModel.updateNome(it) },
            label = { Text("Nome (para Registro)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // Campo de Email
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { usuarioViewModel.updateEmail(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))

        // Campo de Senha
        OutlinedTextField(
            value = uiState.senha,
            onValueChange = { usuarioViewModel.updateSenha(it) },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth()
        )

        // Exibir Erros
        uiState.error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(Modifier.height(16.dp))

        // Botões de Ação
        Button(
            onClick = {
                usuarioViewModel.viewModelScope.launch {
                    val userId = usuarioViewModel.logarUsuario() // Tenta Logar
                    if (userId != null) {
                        onLoginSuccess(userId)
                    } else {
                        // Se falhar, tenta Registrar
                        val newUserId = usuarioViewModel.registrarUsuario()
                        if (newUserId != null) {
                            onLoginSuccess(newUserId)
                        }
                    }
                }
            },
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState.isLoading) "Processando..." else "LOGIN / REGISTRAR")
        }

        Spacer(Modifier.height(16.dp))

        // Botão Pular (Modo Convidado)
        TextButton(onClick = onSkip, enabled = !uiState.isLoading) {
            Text("PULAR (Modo Convidado)")
        }
    }
}