package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.register

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.ActivityRegisterBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.LoginActivity
import cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel.RegisterViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import android.R.bool
import kotlinx.coroutines.tasks.await


class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
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

        //test()
        setup()

    }

    private fun setup() {

        var checkDni: Boolean
        var checkDni2 = false
        var checkMail: Boolean
        var checkName: Boolean
        var checkPasswd: Boolean
        var improvePassw = "Prodis"


        //Accedim al botó de registrar-se i escoltem l'esdevniment
        binding.btnRegistro.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                //Comprovem si les dades introduides son correctes
                checkDni = checkDni(binding.etxtRegDni.text.toString())
                checkDni2 = checkDni2(binding.etxtRegDni.text.toString())
                checkMail = checkMail(binding.etxtRegMail.text.toString())
                checkName = checkName(binding.etxtRegNom.text.toString())
                checkPasswd = checkPass(
                    binding.etxtRegCont.text.toString(),
                    binding.etxtRegConfPass.text.toString()0
                )


                //Si totes les dades son correctes registrarem l'usuari
                Log.i("Check", "Mail: "+checkMail.toString())
                Log.i("Check", "Name: "+checkName.toString())
                Log.i("Check", "Password: "+checkPasswd.toString())
                Log.i("Check", "Dni: "+checkDni.toString())
                Log.i("Check", "Dni2: "+checkDni2.toString())
                //Creem el registre amb email i contrasenya...
                if (checkMail && checkName && checkPasswd && checkDni && checkDni2) {
                    //Registrem a l'usuari i amb el mètode addOnCompleteListener,
                    // ens notificarà si el registre a estat un èxit o no.
                    improvePassw += binding.etxtRegCont.text.toString()
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.etxtRegMail.text.toString(), improvePassw
                    ).addOnCompleteListener {
                        if (it.isSuccessful) { //Si el registre ha estat un èxit...
                            showSucces(
                                it.result?.user?.email ?: "",
                                tipusProveidor.BASIC,
                                binding.etxtRegDni.text.toString()
                            )
                            finish()
                        } else { //Si el registre no ha estat un èxit...
                            showAlert()
                        }
                    }.await()

                } else
                    showAlert()
            }
        }
    }


    //Funció que crea l'alert de tipus AlertDialog que es mostrarà si el registre no ha estat un èxit
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("No s'ha pogut completar el registre.")
        builder.setPositiveButton("Aceptar", null)
        builder.show()
    }


    //Funcio que mostra el resultat del registre si ha tingut exit, mitjançant la pantalla Home
    private fun showSucces(email: String, proveidor: tipusProveidor, dni: String) {
        //Creem un objecte Intent passant-li com a paràmetre el context de l'Activitat acual i
        // el nom de la pantalla a la que volem navegar, és a dir, HomeActivity
        val homeIntent: Intent = Intent(this, ConRegistro::class.java).apply {
            putExtra("email", email) //Correu a mostrar
            putExtra(
                "proveidor",
                proveidor.name
            ) //proveidor a mostra. En el nostre cas de moment, només BASIC
            putExtra("dni", dni)
        }

        //Guardar al firestore les dades de nom, dni , correu
        viewModel.saveDB(
            binding.etxtRegNom.text.toString(),
            binding.etxtRegDni.text.toString(),
            binding.etxtRegMail.text.toString()
            //binding.etxtRegTel.text.toString() as Int
        )
        startActivity(homeIntent)

    }

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

    private suspend fun checkDni2(dni: String): Boolean {
        return db.collection("users").whereEqualTo("DNI", dni)
            .get()
            .await().isEmpty
    }

    /*private fun checkDni3(dni: String): Boolean {
        var checker = true
        db.collection("users").whereEqualTo("DNI", dni)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    return@addOnSuccessListener
                } else {
                    for (document in documents) {
                        return@addOnSuccessListener
                    }
                }

            }
        return checker
    }*/
    /*private fun checkDni4(dni: String): Boolean {

        var checker = false
        Log.i("TAG", checker.toString()+" dni1")
        if(db.collection("users")
                .whereEqualTo("DNI", dni)
                .get().isSuccessful() == false){
            checker = true
        }else if(db.collection("users").whereEqualTo("DNI", dni)
                .get().isSuccessful){
            checker = false
        }
        Log.i("TAG", checker.toString()+" dni2")
        return checker
    }*/



    private fun test(){
        if (!checkDni(binding.etxtRegDni.text.toString())){
            binding.etxtRegDni.error = "DNI no valid"
        }
    }

    private fun showError() {
        val errorDis = AlertDialog.Builder(this)
        errorDis.setTitle("Inici de Sessió fallat")
        errorDis.setMessage("DNI o Contrasenya incorrectes!!!")
        errorDis.setPositiveButton("Aceptar", null)
        errorDis.show()
    }
}
