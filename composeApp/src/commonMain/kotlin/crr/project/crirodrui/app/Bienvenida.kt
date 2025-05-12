package ies.sequeros.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import crr.cliente.crirodrui.viewmodels.UsuarioViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import kotlin.coroutines.coroutineContext

@Composable
fun Bienvenida() {
    Text(
        text = "FriendlyTPV Pantalla principal",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 32.dp)
    )
}