package cat.copernic.jose.antonio.miranda.prodiscomtest.util

class CheckUser {
    //Funcion para comprobar el nombre

    private fun checkName(name: String): Boolean {
        val checkName = "^[A-Za-z]*$".toRegex()
        var checker = false
        if (checkName.matches(name)) checker = true

        return checker
    }

    //Funcion para comprobar el email

    private fun checkMail(mail: String): Boolean {
        var checker: Boolean = false
        val checkMail = ("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
                ).toRegex()
        if (checkMail.matches(mail)) checker = true

        return checker
    }

    //Funcion para comprobar la contraseña

    private fun checkPass(passwd: String, checkPasswd: String): Boolean {
        var checker: Boolean = false
        if (passwd.length == 4 && passwd == checkPasswd) checker = true
        return checker
    }

    //Funcion para comprobar el DNI

    private fun checkDni(dni: String): Boolean {
        val regexDni = """[0-9]+[0-9]+[0-9]+[0-9]+[0-9]+[0-9]+[0-9]+[0-9]+[A-Z]""".toRegex()
        var comprobacion: Boolean = false
        if (regexDni.matches(dni)) {
            val dniNum: String = dni.substring(0, 8)
            val resultDni: Int = dniNum.toInt() % 23
            val letraDni: String = dni[8].toString()
            val letraComprobada: String

            when (resultDni) {
                0 -> letraComprobada = "T"
                1 -> letraComprobada = "R"
                2 -> letraComprobada = "W"
                3 -> letraComprobada = "A"
                4 -> letraComprobada = "G"
                5 -> letraComprobada = "M"
                6 -> letraComprobada = "Y"
                7 -> letraComprobada = "F"
                8 -> letraComprobada = "P"
                9 -> letraComprobada = "D"
                10 -> letraComprobada = "X"
                11 -> letraComprobada = "B"
                12 -> letraComprobada = "N"
                13 -> letraComprobada = "J"
                14 -> letraComprobada = "Z"
                15 -> letraComprobada = "S"
                16 -> letraComprobada = "Q"
                17 -> letraComprobada = "V"
                18 -> letraComprobada = "H"
                19 -> letraComprobada = "L"
                20 -> letraComprobada = "C"
                21 -> letraComprobada = "K"
                22 -> letraComprobada = "E"

                else -> letraComprobada = ""
            }

            if (letraDni == letraComprobada) comprobacion = true
        }

        return comprobacion

    }
}