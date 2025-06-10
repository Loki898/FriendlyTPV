package crr.project.crirodrui.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import crr.cliente.crirodrui.viewmodels.UserViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Usuario(){
    val userViewModel: UserViewModel = koinViewModel()
    val user=userViewModel.selected.value.user
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Información del usuario",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text("ID: ${user.id ?: "Sin ID"}")
            Text("Nombre de usuario: ${user.username}")
            Text("Nombre completo: ${user.name}")
            Text("Rol: ${user.role.name}")

        }
    }
    Button(onClick = {}){
        Icon(Icons.Filled.Edit,null)
    }
}