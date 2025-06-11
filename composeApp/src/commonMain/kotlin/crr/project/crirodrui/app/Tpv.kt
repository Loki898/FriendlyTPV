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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import crr.cliente.crirodrui.viewmodels.CategoryViewModel
import crr.cliente.crirodrui.viewmodels.ProductViewModel
import crr.cliente.crirodrui.viewmodels.UserViewModel
import crr.project.crirodrui.elements.Invoice
import crr.project.crirodrui.elements.LineDetail
import crr.project.crirodrui.elements.LineDetail2
import crr.project.crirodrui.elements.Producto
import crr.project.crirodrui.viewmodels.InvoiceViewModel
import org.koin.compose.viewmodel.koinViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDateTime
import java.time.ZoneId
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory
import java.awt.image.BufferedImage
import java.time.format.DateTimeFormatter

@Composable
fun tpv() {
    val categoryViewModel: CategoryViewModel = koinViewModel()
    val categorias by categoryViewModel.categories.collectAsState()
    val productos by categoryViewModel.products.collectAsState()
    val invoiceViewModel: InvoiceViewModel = koinViewModel()
    val productViewModel: ProductViewModel = koinViewModel()
    val sales_lines by invoiceViewModel.sales_lines.collectAsState()
    val total_price by invoiceViewModel.total_price.collectAsState()
    val invoices = invoiceViewModel.invoices.collectAsState()
    val userViewModel = koinViewModel<UserViewModel>()
    val operator = userViewModel.selected.value.user
    var expanded by remember { mutableStateOf(false) }
    var selectedInvoice = invoiceViewModel.selected.collectAsState()
    var idActualInvoice=invoiceViewModel.idActualInvoice.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(8.dp).border(2.dp, Color.Cyan)) {

        Row(Modifier.weight(1f).border(2.dp, Color.Cyan)) {
            LazyColumn(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
            ) {
                items(sales_lines.size) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(Color.LightGray)
                            .padding(8.dp)
                    ) {
                        Row {
                            Text(
                                sales_lines[it].producto.nombre + "," + sales_lines[it].producto.precio + "€",
                                modifier = Modifier.weight(1f)
                            )
                            Button(
                                onClick = {
                                    invoiceViewModel.removeLine(sales_lines[it])
                                },
                                modifier = Modifier.align(Alignment.CenterVertically)
                            ) {
                                Icon(Icons.Filled.Delete, null)
                            }
                        }

                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight().border(2.dp, Color.Cyan),
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

                Button(onClick = {
                    var baseImponible=0.0
                    var totalIva =0.0
                    sales_lines.forEach {
                        baseImponible = baseImponible+ it.producto.precio!!
                        totalIva= totalIva+ it.producto.precio!! * 0.21
                    }
                    val invoice = Invoice(
                        numSerie = invoiceViewModel.allInvoices.value.size,
                        fechaEmision = LocalDateTime.now(ZoneId.of("Europe/Madrid")),
                        baseImponible = baseImponible,
                        totalIva = totalIva,
                        total = totalIva+baseImponible,
                        algoritmoCifrado ="SHA256",
                        operador = userViewModel.selected.value.user.id,
                        formaPago = 1
                    )
                    invoiceViewModel.sendInvoice(invoice)
                    invoiceViewModel.removeActualInvoice(selectedInvoice.value)
                    invoiceViewModel.deleteLines(invoice)
                    generateInvoicePDFBytes(invoice,sales_lines)
                }, enabled = selectedInvoice.value.idInvoice != 0 && sales_lines.size>0, modifier = Modifier.padding(4.dp)) {
                    Text("Terminar venta")
                }
                Button(onClick = {
                    val invoice = Invoice(
                        idInvoice = idActualInvoice.value,
                        numSerie = 1,
                        totalIva = 0.0,
                        algoritmoCifrado = "01",
                        operador = operator.id,
                        formaPago = 1
                    )
                    invoiceViewModel.addOneToId()
                    invoiceViewModel.selected.value = invoice
                    invoiceViewModel.addInvoice(invoice)
                    invoiceViewModel.loadLines(invoice)
                }, modifier = Modifier.padding(4.dp)) {
                    Text("Nuevo pedido")
                }
                Button(onClick = {
                    invoiceViewModel.removeActualInvoice(selectedInvoice.value)
                    invoiceViewModel.sustractOneToId()
                },enabled = selectedInvoice.value.idInvoice != 0 , modifier = Modifier.padding(4.dp)) {
                    Text("Cancelar pedido")
                }
            }
        }

        Row(Modifier.weight(1f).border(2.dp, Color.Cyan)) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier
                    .weight(2f)
                    .padding(8.dp).border(2.dp, Color.Cyan)
            ) {
                items(productos.size) {
                    val product = productos[it]
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(80.dp)
                            .background(Color(0xFFFFCCBB), shape = RoundedCornerShape(12.dp))
                            .clickable(enabled = selectedInvoice.value.idInvoice != 0) {
                                val actual_category = categoryViewModel.selected.value
                                productViewModel.setCategory(actual_category)
                                val lineDetail = LineDetail(
                                    numeroLinea = invoiceViewModel.sales_lines.value.size,
                                    venda_id = invoiceViewModel.selected.value.idInvoice,
                                    producto = product.id_producto,
                                    precio_uni = product.precio,
                                    cantidad = 1,
                                    subtotal = product.precio,
                                )
                                val lineDetailP = LineDetail2(
                                    lineDetail, product
                                )
                                invoiceViewModel.addLine(lineDetailP)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(product.nombre + "," + product.precio)
                    }
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp).border(2.dp, Color.Cyan)
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
            .clickable(enabled = selectedInvoice.value.idInvoice != 0) {
                categoryViewModel.setSelectedByName(nombre)
                productViewModel.setProducts(products.value)
            }
            .background(Color(0xFF66DDEE), shape = RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(nombre)
    }
}

fun generateInvoicePDFBytes(invoice: Invoice, salesLineDetail2: List<LineDetail2>): ByteArray {
    val localDate = invoice.fechaEmision?.toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val localDateFormated = localDate?.format(formatter) ?: ""

    val doc = PDDocument()
    val page = PDPage()
    doc.addPage(page)

    val content = PDPageContentStream(doc, page)
    val margin = 50f
    var yPosition = 750f

    // Título
    content.beginText()
    content.setFont(PDType1Font.HELVETICA_BOLD, 20f)
    content.newLineAtOffset(margin, yPosition)
    content.showText("FACTURA")
    content.endText()

    yPosition -= 40f

    // Información empresa
    content.beginText()
    content.setFont(PDType1Font.HELVETICA, 12f)
    content.newLineAtOffset(margin, yPosition)
    content.showText("FriendlyTPV")
    content.endText()

    yPosition -= 15f

    content.beginText()
    content.newLineAtOffset(margin, yPosition)
    content.showText("Dirección: Calle Ejemplo 123")
    content.endText()

    yPosition -= 10f
    println(String.format("%.2f", invoice.total))
    val importeFormateado= String.format("%.2f", invoice.total).replace(",",".")
    // Dibujar QR alineado a la izquierda, justo debajo de la info empresa
    val qr = "https://prewww2.aeat.es/wlpl/TIKE-CONT/ValidarQR?nif=20521995S&numserie=${invoice.numSerie}&fecha=${localDateFormated}&importe=${importeFormateado}"
    val qrSize = 100f
    val qrImage = generateQRCodeImage(qr, qrSize.toInt(), qrSize.toInt())
    val pdImage = LosslessFactory.createFromImage(doc, qrImage)
    val qrX = margin
    val qrY = yPosition - qrSize
    content.drawImage(pdImage, qrX, qrY, qrSize, qrSize)

    // Bajamos la yPosition debajo del QR para continuar con el texto
    yPosition = qrY - 20f

    // Datos factura (número y fecha)
    content.beginText()
    content.setFont(PDType1Font.HELVETICA_BOLD, 14f)
    content.newLineAtOffset(margin, yPosition)
    content.showText("Número de Factura: ${invoice.idInvoice}")
    content.endText()

    yPosition -= 20f

    content.beginText()
    content.setFont(PDType1Font.HELVETICA, 12f)
    content.newLineAtOffset(margin, yPosition)
    content.showText("Fecha: $localDateFormated")
    content.endText()

    yPosition -= 30f

    // Productos
    val productos = salesLineDetail2.map { it.producto }
    val products: List<List<String>> = productos.map { producto ->
        val precio = producto.precio ?: 0.0
        val precioConIva = precio * 1.12  // IVA 12% como pediste
        listOf(
            producto.nombre ?: "",
            String.format("%.2f", precio),
            String.format("%.2f", precioConIva)
        )
    }

    // Encabezados tabla
    content.beginText()
    content.setFont(PDType1Font.HELVETICA_BOLD, 12f)
    content.newLineAtOffset(margin, yPosition)
    content.showText("Producto          Precio         Precio + IVA")
    content.endText()

    yPosition -= 20f

    content.setFont(PDType1Font.HELVETICA, 12f)
    for (product in products) {
        content.beginText()
        content.newLineAtOffset(margin, yPosition)
        content.showText(product.joinToString("          "))
        content.endText()
        yPosition -= 20f
    }

    yPosition -= 20f

    // Total (usando IVA 12%)
    val totalConIva = productos.sumOf { (it.precio ?: 0.0) * 1.21 }

    content.beginText()
    content.setFont(PDType1Font.HELVETICA_BOLD, 14f)
    content.newLineAtOffset(margin, yPosition)
    content.showText("Total: ${String.format("%.2f", totalConIva)}")
    content.endText()

    content.close()

    val outputStream = ByteArrayOutputStream()
    doc.save(outputStream)
    doc.close()

    File("factura.pdf").writeBytes(outputStream.toByteArray())

    return outputStream.toByteArray()
}


fun generateQRCodeImage(text: String, width: Int, height: Int): BufferedImage {
    val qrCodeWriter = QRCodeWriter()
    val bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
    return MatrixToImageWriter.toBufferedImage(bitMatrix)
}
