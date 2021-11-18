package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.register

data class RegisterFormData(
    val name: String? = null,
    val mail: String? = null,
    val dni: String? = null,
    val passwd: String? = null,
    val passConf: String? = null
)