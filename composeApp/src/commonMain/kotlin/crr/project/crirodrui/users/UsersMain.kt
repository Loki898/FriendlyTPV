package crr.project.crirodrui.users

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import crr.cliente.crirodrui.viewmodels.UserViewModel
import crr.project.crirodrui.elements.User
import ies.sequeros.app.Bienvenida
import org.example.damgramclient.ui.posts.UserItem
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun UserMain(
    modifier: Modifier = Modifier, vm: UserViewModel = koinViewModel(), vmUser: UserViewModel = koinViewModel()
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<User>()
    val elementos by vm.users.collectAsState()
    val selected by vm.selected.collectAsState()
    val isListAndDetailVisible =
        navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded && navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded
    val searchview = navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded
    var nav = remember { mutableStateOf(0) }
    Scaffold(floatingActionButton = {
        if (searchview) {
            FloatingActionButton(onClick = {
                vm.unSelect()
                nav.value = 1
                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)

            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    }) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            ListDetailPaneScaffold(modifier = Modifier,
                directive = navigator.scaffoldDirective,
                value = navigator.scaffoldValue,
                listPane = {
                    Box {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(top = 32.dp)
                        ) {

                            items(elementos.size) {
                                UserItem(
                                    elementos[it],
                                )
                            }
                        }
                    }
                },
                detailPane = {
                    Column(
                        modifier = Modifier

                    ) {
                        Bienvenida()
                    }

                })
        }
    }
}