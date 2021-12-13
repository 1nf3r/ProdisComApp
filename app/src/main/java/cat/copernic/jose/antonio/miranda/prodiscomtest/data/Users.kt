package cat.copernic.jose.antonio.miranda.prodiscomtest.data

import java.io.Serializable

class Users (
    val uid: String = "",
    val userName: String = "",
    var rooms: MutableMap<String, Any>? = null
) : Serializable