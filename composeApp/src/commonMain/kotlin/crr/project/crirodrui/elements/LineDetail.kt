package crr.project.crirodrui.elements

import kotlinx.serialization.Serializable

@Serializable
data class LineDetail(
    var numeroLinea:Int?=0,
    var venda_id:Int?=0,
    var producto:Int?=0,
    var precio_uni:Double?=0.0,
    var cantidad:Int? = 0,
    var subtotal:Double?=0.0,
    var tipo_iva:Int? = 21
)

data class LineDetail2(
    var lineDetail: LineDetail,
    var producto:Producto
)