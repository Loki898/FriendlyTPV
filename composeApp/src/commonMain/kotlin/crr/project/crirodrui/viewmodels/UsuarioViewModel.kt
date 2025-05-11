package crr.cliente.crirodrui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import crr.cliente.crirodrui.repositorios.UserRepository
import crr.project.crirodrui.elements.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

data class UserState(
    var isLogged: Boolean = false,
    var user: User = User(),
    var token: String? = null,
    var isLoginError: Boolean = false,
)

class UsuarioViewModel(private val userRepository: UserRepository) : ViewModel() {
    var selected = MutableStateFlow(UserState())
    var status = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            val userId = selected.value.user.id
            if (userId != null) {
                /*var postsUser = userRepository.getPostsByUserId(userId).toMutableList()
                selected.update { it.copy(posts = postsUser) }*/
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            //val token = userRepository.authUser(username, password)
            val token="token"
            val file = File("token_actual.txt")
            file.writeText(token.toString())

            //if (token != null) {
                val user = userRepository.getUserByUsername(username)
               // val posts = userRepository.getPostsByUserId(user.id!!).toMutableList()
                selected.update {
                    it.copy(
                        token = token, isLoginError = false, isLogged = true, user = user
                    )
                }
           /* } else {
                selected.update {
                    it.copy(
                        token = null, isLoginError = true, isLogged = false, user = User()
                    )
                }
            }*/
        }
    }


    fun register(username: String, password: String) {
        viewModelScope.launch {
            status.value = userRepository.addUser(username, password).value
        }
    }
}