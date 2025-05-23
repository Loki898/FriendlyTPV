package crr.cliente.crirodrui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import crr.cliente.crirodrui.repositorios.UserRepository
import crr.project.crirodrui.elements.Rol
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

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    var selected = MutableStateFlow(UserState())
    var status = MutableStateFlow(0)
    var users = MutableStateFlow(listOf<User>())
    var userSelected = MutableStateFlow(User())

    init {
        viewModelScope.launch {
            users.value = userRepository.getUsers()
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val token = userRepository.authUser(username, password)
            val file = File("token_actual.txt")
            file.writeText(token.toString())

            if (token != null) {
                val user = userRepository.getUserByUsername(username)
                selected.update {
                    it.copy(
                        token = token, isLoginError = false, isLogged = true, user = user
                    )
                }
                userSelected.value = user
            } else {
                selected.update {
                    it.copy(
                        token = null, isLoginError = true, isLogged = false, user = User()
                    )
                }
            }
        }
    }

    fun unSelect() {
        //selected.update { it.copy(user = User()) }
        userSelected.value = User()
    }

    fun setSelected(user: User) {
        //selected.update { it.copy(user = user) }
        userSelected.value = user
    }

    fun register(username: String, password:String,name:String,role:Rol) {
        viewModelScope.launch {
            status.value = userRepository.addUser(username, password,name,role).value
            refreshUsers()
        }
    }
    fun deleteUser(id: String){
        viewModelScope.launch {
            val eliminado = userRepository.deleteUser(id)
            users.value = users.value.filter { it.id != id }
        }
    }

    fun reloadStatus(){
        status.value = 0
    }
    fun errorPasswords(){
        status.value = -1
    }

    private fun refreshUsers(){
        viewModelScope.launch {
            users.value = userRepository.getUsers()
        }

    }
}