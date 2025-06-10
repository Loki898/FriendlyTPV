package crr.project.crirodrui.app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import crr.project.crirodrui.elements.Invoice
import crr.project.crirodrui.viewmodels.InvoiceViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HistoricoFacturas(
    modifier: Modifier = Modifier,
    vm: InvoiceViewModel = koinViewModel()
) {
    vm.getAllInvoices()
    val navigator = rememberListDetailPaneScaffoldNavigator<Invoice>()
    val invoices = vm.allInvoices.collectAsState()
    val selected = vm.selectedAll.collectAsState()
    val isListAndDetailVisible =
        navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded &&
                navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] == PaneAdaptedValue.Expanded

    Scaffold { innerPadding ->
        Column(modifier = modifier.padding(innerPadding)) {
            ListDetailPaneScaffold(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                directive = navigator.scaffoldDirective,
                value = navigator.scaffoldValue,
                listPane = {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(invoices.value.size) {
                            InvoiceItem(invoice = invoices.value[it]) {
                                vm.selectInvoice(invoices.value[it])
                                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                            }
                        }
                    }
                },
                detailPane = {
                    selected.value.let {
                        InvoiceForm(
                            invoice = it,
                            expandido = isListAndDetailVisible,
                            atras = { navigator.navigateTo(ListDetailPaneScaffoldRole.List) },
                        )
                    } ?: Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Selecciona una factura o pulsa + para crear una")
                    }
                }
            )
        }
    }
}
@Composable
fun InvoiceItem(invoice: Invoice, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("Factura: ${invoice.idInvoice}")
            Text("Fecha: ${invoice.fechaEmision}")
            Text("Operador: ${invoice.operador}")
        }
    }
}

@Composable
fun InvoiceForm(
    invoice: Invoice,
    expandido: Boolean,
    atras: () -> Unit,
) {
    Column(modifier = Modifier.padding(16.dp)) {
        if (!expandido) {
            IconButton(onClick = atras) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }
        }
        Text("Detalle de factura", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        Text("ID: ${invoice.idInvoice}")
        Text("Serie: ${invoice.numSerie}")
        Text("Fecha Emisión: ${invoice.fechaEmision}")
        Text("Base Imponible: ${invoice.baseImponible}")
        Text("Total IVA: ${invoice.totalIva}")
        Text("Total: ${invoice.total}")
        Text("Estado: ${invoice.estado}")
        Text("Firma: ${invoice.firmaHash}")
        Text("Hash anterior: ${invoice.hashAnterior}")
        Text("Algoritmo: ${invoice.algoritmoCifrado}")
        Text("Operador: ${invoice.operador}")
        Text("Forma de pago: ${invoice.formaPago}")
    }
}



