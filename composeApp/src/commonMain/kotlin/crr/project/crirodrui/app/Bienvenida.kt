package ies.sequeros.app

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Bienvenida() {
    Text(
        text = "FriendlyTPV Pantalla principal",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 32.dp)
    )
}