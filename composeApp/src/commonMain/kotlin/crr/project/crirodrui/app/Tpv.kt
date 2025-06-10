package crr.project.crirodrui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import crr.cliente.crirodrui.viewmodels.CategoryViewModel
import crr.cliente.crirodrui.viewmodels.ProductViewModel
import crr.cliente.crirodrui.viewmodels.UserViewModel
import crr.project.crirodrui.elements.Category
import crr.project.crirodrui.elements.Invoice
import crr.project.crirodrui.elements.LineDetail
import crr.project.crirodrui.elements.LineDetail2
import crr.project.crirodrui.viewmodels.InvoiceViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun tpv(){
    val categoryViewModel: CategoryViewModel = koinViewModel()
    val categorias by categoryViewModel.categories.collectAsState()
    val productos by categoryViewModel.products.collectAsState()
    val invoiceViewModel: InvoiceViewModel = koinViewModel()
    val productViewModel:ProductViewModel= koinViewModel()
    val sales_lines by invoiceViewModel.sales_lines.collectAsState()
    val total_price by invoiceViewModel.total_price.collectAsState()
    val invoices = invoiceViewModel.invoices.collectAsState()
    val userViewModel = koinViewModel<UserViewModel>()
    val operator = userViewModel.selected.value.user
    var expanded by remember { mutableStateOf(false) }
    var selectedInvoice = invoiceViewModel.selected.collectAsState()
    //categoryViewModel.unSelect()


    // Panel principal
    Column(modifier = Modifier.fillMaxSize().padding(8.dp).border(2.dp,Color.Cyan)) {

        // Parte superior: líneas detalle + total
        Row(Modifier.weight(1f).border(2.dp,Color.Cyan)) {
            // Líneas de detalle
            LazyColumn(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
            ) {
                items(sales_lines.size){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(Color.LightGray)
                            .padding(8.dp)
                    ) {
                        Row {
                            Text(sales_lines[it].producto.nombre+","+sales_lines[it].producto.precio+"€", modifier = Modifier.weight(1f))
                            Button(
                                onClick = {
                                    invoiceViewModel.removeLine(sales_lines[it])
                                },
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ){
                                Icon(Icons.Filled.Delete,null)
                            }
                        }

                    }
                }
            }

            // Total + botones
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight().border(2.dp,Color.Cyan),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text("Seleccionar factura", fontSize = 18.sp)

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(4.dp))
                        .border(1.dp, Color.Gray)
                        .clickable { expanded = true }
                        .padding(8.dp)
                ) {
                    Text("Factura: ${if (selectedInvoice.value.idInvoice == 0) "Ninguna" else selectedInvoice.value.idInvoice}")
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        invoices.value.forEach { invoice ->
                            DropdownMenuItem(
                                text = { Text("Factura ${invoice.idInvoice}") },
                                onClick = {
                                    invoiceViewModel.selected.value = invoice
                                    invoiceViewModel.loadLines(invoice)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Text("Total", fontSize = 24.sp)
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .border(2.dp, Color.Black)
                        .padding(8.dp)
                ) {
                    Text(total_price.toString(), fontSize = 20.sp)
                }

                Button(onClick = { }, modifier = Modifier.padding(4.dp)) {
                    Text("Terminar venta")
                }
                Button(onClick = {
                    val invoice = Invoice(
                        idInvoice = invoiceViewModel.invoices.value.size+1,
                        numSerie = 1,
                        totalIva = 0.0,
                        algoritmoCifrado = "01",
                        operador = operator.id,
                        formaPago = 1
                    )
                    invoiceViewModel.selected.value = invoice
                    invoiceViewModel.addInvoice(invoice)
                    invoiceViewModel.loadLines(invoice)
                }, modifier = Modifier.padding(4.dp)) {
                    Text("Nuevo pedido")
                }
                Button(onClick = { }, modifier = Modifier.padding(4.dp)) {
                    Text("Cancelar pedido")
                }
            }
        }

        // Parte inferior: productos y categorías
        Row(Modifier.weight(1f).border(2.dp,Color.Cyan)) {
            // Productos
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier
                    .weight(2f)
                    .padding(8.dp).border(2.dp,Color.Cyan)
            ) {
                items(productos.size) {
                    val product = productos[it]
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(80.dp)
                            .background(Color(0xFFFFCCBB), shape = RoundedCornerShape(12.dp))
                            .clickable {
                                val actual_category=categoryViewModel.selected.value
                                productViewModel.setCategory(actual_category)
                                val lineDetail = LineDetail(
                                    numeroLinea = invoiceViewModel.sales_lines.value.size,
                                    venda_id = invoiceViewModel.selected.value.idInvoice,
                                    producto = product.id_producto,
                                    precio_uni = product.precio,
                                    cantidad = 1,
                                    subtotal = product.precio,
                                )
                                val lineDetailP=LineDetail2(
                                    lineDetail,product
                                )
                                invoiceViewModel.addLine(lineDetailP)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(product.nombre +","+product.precio)
                    }
                }
            }

            // Categorías
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp).border(2.dp,Color.Cyan)
            ) {
                items(categorias.size) {
                    CategoriaBoton(categorias.get(it).nombre)
                }
            }
        }
    }
}

        @Composable
        fun CategoriaBoton(nombre: String) {
            val categoryViewModel: CategoryViewModel = koinViewModel()
            val productViewModel: ProductViewModel = koinViewModel()
            val invoiceViewModel: InvoiceViewModel = koinViewModel()
            var products = categoryViewModel.products.collectAsState()
            val selectedInvoice = invoiceViewModel.selected.collectAsState()
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(80.dp)
                    .clickable(enabled = selectedInvoice.value.idInvoice!=0) {
                        categoryViewModel.setSelectedByName(nombre)
                        productViewModel.setProducts(products.value)
                    }
                    .background(Color(0xFF66DDEE), shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(nombre)
            }
        }
