package crr.project.crirodrui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.window.core.layout.WindowWidthSizeClass
import crr.cliente.crirodrui.viewmodels.UserViewModel
import crr.project.crirodrui.elements.Rol
import crr.project.crirodrui.products.CategoryMain
import crr.project.crirodrui.users.UserMain
import ies.sequeros.app.Bienvenida
import org.koin.compose.viewmodel.koinViewModel

enum class AppDestinations(
    val label: String, val icon: ImageVector, val contentDescription: String, val visibleCompact: Boolean
) {
    HOME("Inicio", Icons.Default.Home, "Inicio", true), PERFIL(
        "Productos", Icons.Filled.Inventory, "Gestión de productos", true
    ),
    ADMINISTRAR("Usuarios", Icons.Default.AdminPanelSettings, "Usuarios", true),
    SALIR("Salir", Icons.Filled.Logout, "Salir", true),

}

@Composable
fun Principal(modifier: Modifier = Modifier, salir: () -> Unit) {
    var seleted = remember { mutableStateOf(AppDestinations.HOME) }
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val vm: UserViewModel = koinViewModel()
    Column(modifier = modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.error)) {
        NavigationSuiteScaffold(
            modifier = Modifier.background(MaterialTheme.colorScheme.error),
            navigationSuiteItems = {

                AppDestinations.entries.forEach {
                    if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT || it.visibleCompact == true) {
                         if (it.equals(AppDestinations.ADMINISTRAR)) {
                             if (vm.selected.value.user.role == Rol.ADMIN){
                                 item(icon = {
                                     Icon(
                                         imageVector = it.icon,
                                         contentDescription = it.contentDescription,
                                     )
                                 }, label = { Text(it.label) }, selected = seleted.value == it, onClick = {
                                     if (it == AppDestinations.SALIR) salir()
                                     else seleted.value = it
                                 })
                             }
                         } else {
                             item(icon = {
                                 Icon(
                                     imageVector = it.icon,
                                     contentDescription = it.contentDescription,
                                 )
                             }, label = { Text(it.label) }, selected = seleted.value == it, onClick = {
                                 if (it == AppDestinations.SALIR) salir()
                                 else seleted.value = it
                             })
                         }
                    }
                }
            }) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,

                    ) {
                    when (seleted.value) {
                        AppDestinations.HOME -> {
                            Bienvenida()
                        }

                        AppDestinations.PERFIL -> {
                            CategoryMain()
                        }

                        AppDestinations.SALIR -> {
                            Bienvenida()
                        }

                        AppDestinations.ADMINISTRAR -> {
                            UserMain()
                        }
                    }
                }
            }
        }
    }
}