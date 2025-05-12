package crr.cliente.crirodrui.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.Serializable
import org.koin.java.KoinJavaComponent.inject
import java.io.File
import kotlin.coroutines.coroutineContext

@Composable
fun LoginForm(navController: NavHostController, loginFunction: ((String, String) -> Boolean)? =null) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var enabledButton =username.isNotBlank() && password.isNotBlank()


   Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Iniciar Sesión",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },
                placeholder = { Text("Introduce  usuario") },
                singleLine = true,
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                placeholder = { Text("Introduce contraseña") },
                singleLine = true,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        val icon = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(icon, contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña")
                    }
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(
                    onClick = {
                        if (loginFunction != null) {
                            loginFunction(username, password) ?: true
                        }
                    },
                    enabled = enabledButton

                ) {
                    Text("Iniciar Sesión")
                }
                Spacer(modifier = Modifier.width(10.dp))
                /*Button(
                    onClick = {
                        navController.navigate("registro")
                    }
                ) {
                    Text("Registrarse")
                }*/
            }
        }
    }
}

