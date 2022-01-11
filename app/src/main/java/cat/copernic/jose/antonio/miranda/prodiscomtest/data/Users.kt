package cat.copernic.jose.antonio.miranda.prodiscomtest.data

import java.io.Serializable

//Estructura d'un usuari en la col·lecció contactes
data class Users(
    val email: String? = "",
    val nombre: String? = "",
    var rooms: MutableMap<String, Any>? = null,
) : Serializable