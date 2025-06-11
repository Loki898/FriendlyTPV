package crr.project.crirodrui.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import crr.cliente.crirodrui.repositorios.CategoryRepository
import crr.cliente.crirodrui.repositorios.InvoiceRepository
import crr.project.crirodrui.elements.Category
import crr.project.crirodrui.elements.Invoice
import crr.project.crirodrui.elements.LineDetail
import crr.project.crirodrui.elements.LineDetail2
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InvoiceViewModel(val invoiceRepository: InvoiceRepository) : ViewModel() {
    var selected = MutableStateFlow(Invoice())
    var invoices= MutableStateFlow(listOf<Invoice>())
    var sales_lines = MutableStateFlow(listOf<LineDetail2>())
    var salesLinesTotal= MutableStateFlow(listOf<LineDetail2>())
    var total_price = MutableStateFlow(0.0)
    var allInvoices = MutableStateFlow(listOf<Invoice>())
    var selectedAll=MutableStateFlow(Invoice())
    var idActualInvoice = MutableStateFlow(1)

    init {
        getAllInvoices()
    }

    fun addLine(line: LineDetail2) {
        val id = selected.value.idInvoice
        if (id != null && line.lineDetail.precio_uni != null) {
            line.lineDetail.venda_id = id
            sales_lines.update { it + line }
            salesLinesTotal.update { it + line }
            calculateTotal()
        }
    }
    fun removeLine(line: LineDetail2) {
        sales_lines.update { it - line }
        calculateTotal()
    }
    fun addOneToId(){
        idActualInvoice.value++
    }
    fun sustractOneToId(){
        idActualInvoice.value--
    }
    fun addInvoice(invoice: Invoice) {
        invoices.update { it + invoice }
        selected.value=invoice
    }

    fun removeActualInvoice(invoice: Invoice) {
        invoices.update { it - invoice }
        salesLinesTotal.update {
            salesLinesTotal.value-sales_lines.value
        }
        loadLines(Invoice())
    }

    private fun calculateTotal(){
        total_price.value = sales_lines.value.sumOf { it.lineDetail.precio_uni ?: 0.0 }
    }

    fun loadLines(invoice: Invoice) {
        selected.value = invoice
        sales_lines.value = salesLinesTotal.value.filter {
            it.lineDetail.venda_id == invoice.idInvoice
        }
        calculateTotal()
    }

    fun deleteLines(invoice: Invoice){
      salesLinesTotal.value = salesLinesTotal.value.filter {
          it.lineDetail.venda_id != invoice.idInvoice
      }
    }
    fun sendInvoice(invoice: Invoice) {
        viewModelScope.launch {
            invoiceRepository.sendInvoice(invoice)
            getAllInvoices()
        }
    }

    fun getAllInvoices(){
        viewModelScope.launch {
            allInvoices.value = invoiceRepository.getInvoices()
        }
    }

    fun unselectInvoice() {
        selectedAll.value = Invoice()
    }

    fun selectInvoice(invoice: Invoice) {
        selectedAll.value = invoice
    }

}
