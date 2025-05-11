package crr.project.crirodrui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import crr.cliente.crirodrui.app.LoginForm
import crr.cliente.crirodrui.app.RegisterForm
import crr.cliente.crirodrui.viewmodels.UsuarioViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController, startDestination = "login"
        ) {
            composable("login") {
                val viewModel: UsuarioViewModel = koinViewModel()
                val state = viewModel.selected.collectAsState()

                var username by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }

                LaunchedEffect(username, password) {
                    if (username.isNotEmpty() && password.isNotEmpty()) {
                        viewModel.login(username, password)
                    }
                }
                if (state.value.isLoginError) {
                    Text("Error al logearse", color = Color.Red)
                    LoginForm(navController) { u, p ->
                        username = u
                        password = p
                        true
                    }
                } else if (!state.value.isLogged) {
                    LoginForm(navController) { u, p ->
                        username = u
                        password = p
                        true
                    }
                } else {
                    navController.navigate("principal")
                    /*Principal(salir = {
                        navController.navigate("login")
                    })*/
                }
            }
            composable("principal") {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Iniciar Sesión",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
            }
            composable("registro") {
                val viewModel: UsuarioViewModel = koinViewModel()

                var username by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }


                RegisterForm(navController) { u, p ->
                    username = u
                    password = p
                    viewModel.register(username, password)
                    true
                }
            }
        }

    }
}