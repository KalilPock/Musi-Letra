package com.example.musiletra.ui

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.musiletra.data.database.AppDatabase
import com.example.musiletra.data.database.Usuario
import kotlinx.coroutines.launch
import java.security.MessageDigest

data class AuthUiState(
    val email: String = "",
    val senha: String = "",
    val nome: String = "",
    val error: String? = null,
    val isLoading: Boolean = false
)

class UsuarioViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val usuarioDao = database.usuarioDao()

    var uiState by mutableStateOf(AuthUiState())
        private set

    var usuarioLogado: Usuario? by mutableStateOf(null)
        private set

    /**
     * Função para fazer hash simples da senha (SHA-256)
     */
    private fun hashSenha(senha: String): String {
        val bytes = senha.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }

    fun updateEmail(email: String) {
        uiState = uiState.copy(email = email, error = null)
    }

    fun updateSenha(senha: String) {
        uiState = uiState.copy(senha = senha, error = null)
    }

    fun updateNome(nome: String) {
        uiState = uiState.copy(nome = nome, error = null)
    }

    /**
     * Tenta fazer login
     */
    suspend fun logarUsuario(): Usuario? {
        if (uiState.email.isBlank() || uiState.senha.isBlank()) {
            uiState = uiState.copy(error = "Preencha email e senha")
            return null
        }

        uiState = uiState.copy(isLoading = true)

        return try {
            val senhaHash = hashSenha(uiState.senha)
            val usuario = usuarioDao.login(uiState.email, senhaHash)

            if (usuario != null) {
                usuarioLogado = usuario
                uiState = uiState.copy(isLoading = false, error = null)
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Email ou senha incorretos"
                )
            }
            usuario
        } catch (e: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                error = "Erro ao fazer login: ${e.message}"
            )
            null
        }
    }

    /**
     * Registra novo usuário
     */
    suspend fun registrarUsuario(): Usuario? {
        if (uiState.nome.isBlank() || uiState.email.isBlank() || uiState.senha.isBlank()) {
            uiState = uiState.copy(error = "Preencha todos os campos")
            return null
        }

        uiState = uiState.copy(isLoading = true)

        return try {
            // Verifica se email já existe
            if (usuarioDao.getByEmail(uiState.email) != null) {
                uiState = uiState.copy(
                    isLoading = false,
                    error = "Email já cadastrado"
                )
                return null
            }

            val senhaHash = hashSenha(uiState.senha)
            val novoUsuario = Usuario(
                nome = uiState.nome,
                email = uiState.email,
                senhaHash = senhaHash
            )

            val userId = usuarioDao.inserir(novoUsuario)
            val usuarioSalvo = usuarioDao.getByEmail(uiState.email)

            if (usuarioSalvo != null) {
                usuarioLogado = usuarioSalvo
                uiState = uiState.copy(isLoading = false, error = null)
            }

            usuarioSalvo
        } catch (e: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                error = "Erro ao registrar: ${e.message}"
            )
            null
        }
    }

    fun logout() {
        usuarioLogado = null
        uiState = AuthUiState()
    }
}
