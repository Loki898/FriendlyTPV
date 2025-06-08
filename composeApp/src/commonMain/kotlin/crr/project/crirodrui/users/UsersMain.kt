package crr.project.crirodrui.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import crr.cliente.crirodrui.viewmodels.UserViewModel
import crr.project.crirodrui.app.RegisterFun
import crr.project.crirodrui.elements.User
import ies.sequeros.app.Bienvenida
import org.example.damgramclient.ui.posts.UserItem
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun UserMain(
    modifier: Modifier = Modifier,
    vm: UserViewModel = koinViewModel(),
    vmUser: UserViewModel = koinViewModel()
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<User>()
    val elementos by vm.users.collectAsState()
    val selected by vm.userSelected.collectAsState()
    //var editing by remember { mutableStateOf(false) }
    val editing = selected.id?.isNotBlank() ?: false
    val isListAndDetailVisible =
        navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded &&
                navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded
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
            ListDetailPaneScaffold(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                directive = navigator.scaffoldDirective,
                value = navigator.scaffoldValue,
                listPane = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = Modifier
                        ) {
                            items(elementos.size) {
                                UserItem(
                                    elementos[it],
                                    onClick = {
                                        vm.setSelected(elementos[it])
                                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                                    }
                                )
                            }
                        }
                    }
                },
                detailPane = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.TopStart,
                    ) {
                        Column(
                            modifier = Modifier
                        ) {
                            RegisterFun(
                                selected = selected,
                                expandido = isListAndDetailVisible,
                                atras = { navigator.navigateTo(ListDetailPaneScaffoldRole.List) },
                               editing = editing
                            )
                        }
                    }
                }
            )
        }
    }
}
