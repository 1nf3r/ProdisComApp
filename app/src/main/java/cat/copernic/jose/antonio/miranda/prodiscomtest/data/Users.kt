package cat.copernic.jose.antonio.miranda.prodiscomtest.data

import java.io.Serializable

data class Users(
    val email: String? = "",
    val Nombre: String? = "",
    var rooms: MutableMap<String, Any>? = null,
//    var roomId: String? = null
) : Serializable