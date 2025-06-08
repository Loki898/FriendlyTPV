package crr.project.crirodrui.products

//import androidx.compose.ui.graphics.toPainter
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
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

@OptIn(ExperimentalEncodingApi::class)
@Composable
fun ProductItem(
    item: Producto,
    vm: CategoryViewModel = koinViewModel(),
    /*onClick: () -> Unit,
    vmUser: UserViewModel = koinViewModel(),
    userRep: UserRepository = UserRepository()*/
) {
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Row {
            item.nombre?.let { Text(it) }
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}
