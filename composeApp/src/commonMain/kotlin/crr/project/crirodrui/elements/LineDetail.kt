package crr.project.crirodrui.elements

import kotlinx.serialization.Serializable

@Serializable
data class LineDetail(
    var numeroLinea:Int,
    var venda_id:Int,
    var producto:Int,
    var precio_uni:Double?,
    var cantidad:Int? = 0,
    var subtotal:Double?,
    var tipo_iva:Int? = 21
)