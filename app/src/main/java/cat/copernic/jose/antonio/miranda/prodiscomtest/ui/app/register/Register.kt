package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.register

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.ActivityRegisterBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern.compile

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Al clicar en necesitas ayuda saldra un pop up.
        val builder = AlertDialog.Builder(this)
        binding.txtVAjuda2.setOnClickListener {
            builder.setTitle("Ajuda")
            builder.setMessage(
                "Hauras d'introduir el DNI, Contrasenya, Email i acceptar les condicions de servei," +
                        " la informacio familiar es opcional." +
                        "\nSi ja t'has registrat clica en Inicia Sessio."
            )
            builder.setPositiveButton("Aceptar", null)
            builder.show()
        }

        //Al clicar vuelves a la pantalla iniciar sesion
        binding.txtVInicia.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        
        setup()

    }

    private fun setup() {

        var checkDni: Boolean

        //Accedim al botó de registrar-se i escoltem l'esdevniment
        binding.btnRegistro.setOnClickListener {
            checkDni = checkDni(binding.etxtRegDni.text.toString())
            //Si s'han introduit el correu i contrasenya
            if ((binding.etxtRegMail.text.isNotEmpty()
                        && binding.etxtRegCont.text.isNotEmpty()) && checkDni
            ) { //Creem el registre amb email i contrasenya...

                //Registrem a l'usuari i amb el mètode addOnCompleteListener, ens notificarà si el registre a estat un èxit o no.
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.etxtRegMail.text.toString(),
                    binding.etxtRegCont.text.toString()
                ).addOnCompleteListener {

                    if (it.isSuccessful) { //Si el registre ha estat un èxit...
                        showSucces(it.result?.user?.email ?: "", tipusProveidor.BASIC)
                        finish()
                    } else { //Si el registre no ha estat un èxit...
                        showAlert()
                    }

                }

            } else
                showAlert()
        }
    }


    //Funció que crea l'alert de tipus AlertDialog que es mostrarà si el registre no ha estat un èxit
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(
            "No s'ha pogut completar el registre."
        )
        builder.setPositiveButton("Aceptar", null)
        builder.show()
    }


    //Funcio que mostra el resultat del registre si ha tingut exit, mitjançant la pantalla Home
    private fun showSucces(email: String, proveidor: tipusProveidor) {
        //Creem un objecte Intent passant-li com a paràmetre el context de l'Activitat acual i el nom de la pantalla a la que volem navegar, és a dir, HomeActivity
        val homeIntent: Intent = Intent(this, ConRegistro::class.java).apply {
            putExtra("email", email) //Correu a mostrar
            putExtra(
                "proveidor",
                proveidor.name
            ) //proveidor a mostra. En el nostre cas de moment, només BASIC
        }

        // Create a new user with a first and last name
        viewModel.saveDB(
            binding.etxtRegNom.text.toString(),
            binding.etxtRegDni.text.toString(),
            binding.etxtRegMail.text.toString(),
            // binding.etxtRegCont.text.toString(),
            binding.editTextTextPersonName5.text.toString()
        )
        startActivity(homeIntent)

    }

   /*  private fun comprobar() {
          if (!comprobarMail(binding.txtRegMail.text.toString())) {
              binding.txtRegMail.error =
                  "Mail no valid" //Muesta el error dentro del input text con el icono rojo
              //binding.btnRegistro.isEnabled = false //desactiva el boton de registro
          }

      }*/

    //Funcion para comprobar el nombre

    private fun checkName(name:String) :Boolean {
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
        if (checkMail.matches(mail))  checker = true

        return checker
    }

    //Funcion para comprobar la contraseña

    private fun checkPass(passwd: String, checkPasswd: String): Boolean {
        var checker :Boolean = false
        if (passwd == checkPasswd) checker = true

        return checker
    }

    //Funcion para comprobar el DNI

    private fun checkDni(dni: String): Boolean {
        val regexDni = """[0-9]+[0-9]+[0-9]+[0-9]+[0-9]+[0-9]+[0-9]+[0-9]+[A-Z]""".toRegex()
        var comprobacion: Boolean = false
        if (regexDni.matches(dni)) {
            val dniNum: String = dni.substring(0, 8)
            Log.d("TAG2", dniNum)// falla aqui
            val resultDni: Int = dniNum.toInt() % 23
            val letraDni: String = dni[8].toString()
            var letraComprobada: String


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
