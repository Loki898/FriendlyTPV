package crr.project.crirodrui.elements

import kotlinx.serialization.Serializable

@Serializable
data class Category (
    var id_category:Int = 0,
    var nombre:String,
){
    constructor(): this(0,"")
}