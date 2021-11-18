package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.register

data class RegisterFormData(
    val name: String,
    val mail: String,
    val dni: String,
    val passwd: String,
    val passConf: String
)