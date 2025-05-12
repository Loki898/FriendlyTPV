package crr.project.crirodrui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import crr.cliente.crirodrui.repositorios.UserRepository
import crr.cliente.crirodrui.viewmodels.UserState
import crr.project.crirodrui.elements.Rol
import crr.project.crirodrui.elements.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class UsuariosViewModel(private val userRepository: UserRepository) : ViewModel() {
    var selected = MutableStateFlow(User())
    var status = MutableStateFlow(0)
    var users = MutableStateFlow(listOf<User>())

    init {
        viewModelScope.launch {
            users.value = userRepository.getUsers()
        }
    }

    fun register(username: String, password: String, name: String, role: Rol) {
        viewModelScope.launch {
            status.value = userRepository.addUser(username, password,name,role).value
        }
    }

}