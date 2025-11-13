package com.example.musiletra.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.musiletra.data.database.Usuario
import com.example.musiletra.data.database.UsuarioDao
import kotlinx.coroutines.launch

// Um estado simples para a UI
data class AuthUiState(
    val email: String = "",
    val senha: String = "",
    val nome: String = "",
    val error: String? = null,
    val isLoading: Boolean = false
)

class UsuarioViewModel(private val usuarioDao: UsuarioDao) : ViewModel() {

    // O estado da tela será guardado aqui
    var uiState = AuthUiState()
        private set

    /**
     * Tenta registrar um novo usuário no banco de dados.
     * @return O ID do novo usuário (Int) ou null se falhar.
     */
    suspend fun registrarUsuario(): Int? {
        if (uiState.email.isBlank() || uiState.senha.isBlank()) {
            uiState = uiState.copy(error = "Preencha todos os campos.", isLoading = false)
            return null
        }

        uiState = uiState.copy(isLoading = true)

        return try {
            // Verificação básica de usuário existente (apenas para exemplo)
            if (usuarioDao.getUsuarioPorEmail(uiState.email) != null) {
                uiState = uiState.copy(error = "E-mail já registrado.", isLoading = false)
                return null
            }

            // Criar e inserir o novo usuário no Room
            val novoUsuario = Usuario(
                nome = uiState.nome,
                email = uiState.email,
                senhaHash = uiState.senha // Idealmente deve ser hasheada!
            )
            usuarioDao.inserirUsuario(novoUsuario)

            // Como o Room não retorna o ID gerado automaticamente via @Insert simples,
            // Precisamos buscar o ID. Para simplificar no contexto da faculdade,
            // podemos apenas assumir sucesso ou buscar novamente.
            // Para código de produção, a função DAO retornaria o ID.
            val usuarioSalvo = usuarioDao.getUsuarioPorEmail(uiState.email)
            uiState = uiState.copy(isLoading = false)
            usuarioSalvo?.id

        } catch (e: Exception) {
            uiState = uiState.copy(error = "Erro ao registrar: ${e.message}", isLoading = false)
            null
        }
    }

    // Função de login (básica)
    suspend fun logarUsuario(): Int? {
        // Implementar lógica similar ao registrar, mas com WHERE email AND senha
        return null // Implementar depois
    }

    // Funções para atualizar os campos da UI
    fun updateEmail(email: String) { uiState = uiState.copy(email = email, error = null) }
    fun updateSenha(senha: String) { uiState = uiState.copy(senha = senha, error = null) }
    fun updateNome(nome: String) { uiState = uiState.copy(nome = nome, error = null) }
}

// Lembre-se: O ViewModel precisa da sua própria Factory!
class UsuarioViewModelFactory(
    private val usuarioDao: UsuarioDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UsuarioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UsuarioViewModel(usuarioDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}