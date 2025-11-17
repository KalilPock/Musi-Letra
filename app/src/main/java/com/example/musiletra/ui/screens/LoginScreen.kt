package com.example.musiletra.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.musiletra.ui.UsuarioViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    usuarioViewModel: UsuarioViewModel,
    onLoginSuccess: () -> Unit,
    onSkip: () -> Unit
) {
    val uiState = usuarioViewModel.uiState
    val scope = rememberCoroutineScope()
    var isRegistering by remember { mutableStateOf(false) }
    var senhaVisivel by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Musi-Letra",
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo/Ícone
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = if (isRegistering) "Criar Conta" else "Bem-vindo!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (isRegistering)
                    "Preencha os campos para se cadastrar"
                else
                    "Entre com sua conta para continuar",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(32.dp))

            // Campo Nome (apenas no registro)
            if (isRegistering) {
                OutlinedTextField(
                    value = uiState.nome,
                    onValueChange = { usuarioViewModel.updateNome(it) },
                    label = { Text("Nome Completo") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(Modifier.height(16.dp))
            }

            // Campo Email
            OutlinedTextField(
                value = uiState.email,
                onValueChange = { usuarioViewModel.updateEmail(it) },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            // Campo Senha
            OutlinedTextField(
                value = uiState.senha,
                onValueChange = { usuarioViewModel.updateSenha(it) },
                label = { Text("Senha") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                trailingIcon = {
                    IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                        Icon(
                            imageVector = if (senhaVisivel)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (senhaVisivel) "Ocultar senha" else "Mostrar senha"
                        )
                    }
                },
                visualTransformation = if (senhaVisivel)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Exibir Erros
            if (uiState.error != null) {
                Spacer(Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            uiState.error,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Botão Principal
            Button(
                onClick = {
                    scope.launch {
                        if (isRegistering) {
                            val usuario = usuarioViewModel.registrarUsuario()
                            if (usuario != null) {
                                onLoginSuccess()
                            }
                        } else {
                            val usuario = usuarioViewModel.logarUsuario()
                            if (usuario != null) {
                                onLoginSuccess()
                            }
                        }
                    }
                },
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = if (isRegistering) "Cadastrar" else "Entrar",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Botão Alternar Login/Registro
            TextButton(
                onClick = { isRegistering = !isRegistering },
                enabled = !uiState.isLoading
            ) {
                Text(
                    if (isRegistering)
                        "Já tem conta? Fazer login"
                    else
                        "Não tem conta? Cadastre-se"
                )
            }

            Spacer(Modifier.height(8.dp))

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Botão Pular (Modo Convidado)
            OutlinedButton(
                onClick = onSkip,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.PersonOff,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Continuar como Convidado")
            }
        }
    }
}
