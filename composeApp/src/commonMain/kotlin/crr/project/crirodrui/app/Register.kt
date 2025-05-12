package crr.project.crirodrui.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import crr.cliente.crirodrui.viewmodels.UsuarioViewModel
import crr.project.crirodrui.elements.Rol
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterFun() {
    val scope = CoroutineScope(Dispatchers.Default)
    val viewModel: UsuarioViewModel = koinViewModel()
    var succes = viewModel.status.collectAsState()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var selectedRol by remember { mutableStateOf(Rol.OPERATOR) }
    var expanded by remember { mutableStateOf(false) }
    var confirmation by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var enabledButton = username.isNotBlank() && password.isNotBlank()

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Registrar",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp),
            )
            if (succes.value >= 400) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Error") },
                    text = { Text("Ese usuario ya existe.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showDialog = false
                            viewModel.reloadStatus()
                        }) {
                            Text("OK")
                        }
                    }
                )
            }
            if (succes.value < 0) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Error") },
                    text = { Text("Las contraseñas no coinciden.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showDialog = false
                            viewModel.reloadStatus()
                        }) {
                            Text("OK")
                        }
                    }
                )
            }
            if (succes.value == HttpStatusCode.Created.value) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Información") },
                    text = { Text("El usuario ha sido registrado correctamente.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showDialog = false
                            username = ""
                            password = ""
                            confirmation = ""
                            name = ""
                            selectedRol = Rol.OPERATOR
                            viewModel.reloadStatus()
                        }) {
                            Text("OK")
                        }
                    }
                )
            }

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },

                placeholder = { Text("Introduce  usuario") },
                singleLine = true,
            )
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },

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
                        Icon(
                            icon,
                            contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
            )

            OutlinedTextField(
                value = confirmation,
                onValueChange = { confirmation = it },
                label = { Text("Confirmar contraseña") },
                placeholder = { Text("Introduce contraseña") },
                singleLine = true,
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        val icon = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(
                            icon,
                            contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                        )
                    }
                },
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedRol.name,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Rol") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    Rol.values().forEach { rol ->
                        DropdownMenuItem(
                            text = { Text(rol.name) },
                            onClick = {
                                selectedRol = rol
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (confirmation==password){
                        viewModel.register(
                            username = username,
                            password = password,
                            name = name,
                            role = selectedRol
                        )
                    }else{
                        viewModel.errorPasswords()
                    }

                }, enabled = enabledButton

            ) {
                Text("Registrarse")
            }
        }
    }
}