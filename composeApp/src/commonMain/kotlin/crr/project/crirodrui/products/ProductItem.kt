package crr.project.crirodrui.products

//import androidx.compose.ui.graphics.toPainter
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import crr.cliente.crirodrui.repositorios.UserRepository
import crr.cliente.crirodrui.viewmodels.CategoryViewModel
import crr.cliente.crirodrui.viewmodels.UserViewModel
import crr.project.crirodrui.elements.Category
import crr.project.crirodrui.elements.Producto
import crr.project.crirodrui.elements.User
import org.koin.compose.viewmodel.koinViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.io.encoding.ExperimentalEncodingApi


@Composable
fun ProductItem(
    item: Producto,
    vm: CategoryViewModel = koinViewModel()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Producto: ${item.nombre ?: "Sin nombre"}",
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Eliminar producto",
                tint = Color.Black
            )
        }
    }
}
