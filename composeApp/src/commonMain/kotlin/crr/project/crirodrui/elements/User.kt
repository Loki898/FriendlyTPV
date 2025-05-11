package crr.project.crirodrui.elements

import kotlinx.serialization.Serializable

@Serializable
enum class Rol(){
    ADMIN,
    OPERATOR
}

@Serializable
data class User(
    var id: String? = null,
    var username: String = "",
    var password: String = "",
    var name: String = "",
    var role: Rol = Rol.OPERATOR
) {
    constructor(): this(null, "", "","")
}