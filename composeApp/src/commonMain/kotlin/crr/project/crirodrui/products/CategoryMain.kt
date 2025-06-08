package crr.project.crirodrui.products

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import crr.cliente.crirodrui.viewmodels.CategoryViewModel
import crr.project.crirodrui.elements.Category
import crr.project.crirodrui.elements.Producto
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.ui.text.input.KeyboardType
import crr.project.crirodrui.elements.Rol
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun CategoryMain(
    modifier: Modifier = Modifier,
    vm: CategoryViewModel = koinViewModel()
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Category>()
    val elementos by vm.categories.collectAsState()
    val selected by vm.userSelected.collectAsState()
    val products by vm.products.collectAsState()

    val editing = selected.id?.isNotBlank() ?: false
    val searchview = navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

    var isAddingCategory by remember { mutableStateOf(false) }
    var isAddingProduct by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            if (searchview) {
                FloatingActionButton(onClick = {
                    showDialog = true
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {

            // Cuadro de diálogo para elegir entre categoría o producto
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("¿Qué deseas añadir?") },
                    text = { Text("Selecciona si deseas añadir una nueva categoría o un producto.") },
                    confirmButton = {
                        TextButton(onClick = {
                            showDialog = false
                            isAddingCategory = true
                        }) {
                            Text("Añadir categoría")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showDialog = false
                            isAddingProduct = true
                        }) {
                            Text("Añadir producto")
                        }
                    }
                )
            }

            when {
                isAddingCategory -> {
                    AddCategoryForm(
                        onCancel = { isAddingCategory = false },
                        onSave = { name ->
                            vm.addCategory(name.toString())
                            isAddingCategory = false
                        }
                    )
                }

                isAddingProduct -> {
                    AddProductForm(
                        onCancel = { isAddingProduct = false },
                        onSave = { producto ->
                            //vm.addCategory(producto)
                            isAddingProduct = false
                        }
                    )
                }

                else -> {
                    ListDetailPaneScaffold(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        directive = navigator.scaffoldDirective,
                        value = navigator.scaffoldValue,
                        listPane = {
                            LazyColumn {
                                items(elementos.size) {
                                    CategoryItem(
                                        elementos[it],
                                        onClick = {
                                            vm.setSelected(elementos[it])
                                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                                        }
                                    )
                                }
                            }
                        },
                        detailPane = {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                items(products.size) {
                                    ProductItem(products[it])
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun AddCategoryForm(onCancel: () -> Unit, onSave: (Any?) -> Unit) {
    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Formulario para añadir categoría")

        // Aquí pondrías campos, por ejemplo:
        var name by remember { mutableStateOf("") }
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre categoría") }
        )

        Spacer(Modifier.height(16.dp))

        Row {
            Button(onClick = { onSave(name) }, enabled = name.isNotBlank()) {
                Text("Guardar")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onCancel) {
                Text("Cancelar")
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductForm(onCancel: () -> Unit, onSave: (Producto) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var tipo_iva by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var vm: CategoryViewModel = koinViewModel()
    var expanded by remember { mutableStateOf(false) }


    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Formulario para añadir producto")

        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
        OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") })
        OutlinedTextField(
            value = precio,
            onValueChange = { nuevoValor ->
                if (nuevoValor.matches(Regex("^\\d*\\.?\\d{0,2}\$"))) {
                    precio = nuevoValor
                }
            },
            label = { Text("Precio") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        OutlinedTextField(
            value = stock,
            onValueChange = { nuevoValor ->
                if (nuevoValor.matches(Regex("^\\d*"))) {
                    stock = nuevoValor
                }
            },
            label = { Text("Stock inicial") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = vm.categories.value.get(0).nombre,
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoría") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                vm.categories.value.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.nombre) },
                        onClick = {
                            categoria = category.id_category.toString()
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))

        Row {
            Button(onClick = {
                onSave(
                    Producto(
                        nombre = nombre,
                        descripcion = descripcion,
                        precio = precio.toDoubleOrNull() ?: 0.0,
                        id_producto = TODO(),
                        stock = TODO(),
                        tipo_iva = TODO(),
                        categoria = TODO()
                    )
                )
            }) {
                Text("Guardar")
            }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onCancel) {
                Text("Cancelar")
            }
        }
    }
}
