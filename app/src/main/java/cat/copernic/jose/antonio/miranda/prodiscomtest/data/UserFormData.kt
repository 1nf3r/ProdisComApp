package cat.copernic.jose.antonio.miranda.prodiscomtest.data

data class UserFormData(
    val name: String? = null,
    val surName:String? = null,
    val mail: String? = null,
    val dni: String? = null,
    val tel: String? = null,
    val passwd: String? = null,
    val passConf: String? = null,
    val imgUser: String? = null,
    val supUser: Boolean? = null
)