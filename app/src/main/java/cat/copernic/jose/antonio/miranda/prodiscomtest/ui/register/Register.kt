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
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.text.isDigitsOnly
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.MainActivity
import kotlinx.coroutines.tasks.await


class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()
    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = Firebase.auth
    private val CHANNEL_ID = "channel_id_example_01"
    private val notificacioId = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        creacioCanalNotificacio()

        //Al clicar en necesitas ayuda saldra un pop up.
        val builder = AlertDialog.Builder(this)
        binding.txtVAjuda2.setOnClickListener {
            builder.setTitle(R.string.help)
            builder.setMessage(R.string.help_info)
            builder.setPositiveButton((R.string.accept), null)
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
        var checkLastName: Boolean
        var checkPasswd: Boolean
        var improvePassw = "prodis"


        //Accedim al botó de registrar-se i escoltem l'esdevniment
        binding.btnRegistro.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                //Comprovem si les dades introduides son correctes
                checkDni = checkDni(binding.etxtRegDni.text.toString())
                checkDni2 = checkDni2(binding.etxtRegDni.text.toString())
                checkMail = checkMail(binding.etxtRegMail.text.toString())
                checkName = checkName(binding.etxtRegNom.text.toString())
                checkLastName = checkLastName(binding.etxtRegCognom.text.toString())
                checkPasswd = checkPass(
                    binding.etxtRegCont.text.toString(),
                    binding.etxtRegConfPass.text.toString()
                )

                //Creem el registre amb email i contrasenya...
                if (checkMail && checkName && checkLastName && checkPasswd && checkDni && checkDni2) {
                    //Registrem a l'usuari i amb el mètode addOnCompleteListener,
                    // ens notificarà si el registre a estat un èxit o no.
                    improvePassw += binding.etxtRegCont.text.toString()
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        binding.etxtRegMail.text.toString(), improvePassw
                    ).addOnCompleteListener {
                        if (it.isSuccessful) { //Si el registre ha estat un èxit...
                            CoroutineScope(Dispatchers.Main).launch {
                                showSucces(
                                    it.result?.user?.email ?: "",
                                    tipusProveidor.BASIC,
                                    binding.etxtRegDni.text.toString()
                                )
                            }

                            finish()
                        } else { //Si el registre no ha estat un èxit...
                            showAlert()
                        }
                    }.await()

                } else if (!checkMail) {
                    showError(R.string.accept)
                } else if (!checkName) {
                    showError(R.string.accept)
                } else if (!checkLastName) {
                    showError(R.string.accept)
                } else if (!checkPasswd) {
                    showError(R.string.accept)
                } else if (!checkDni) {
                    showError(R.string.accept)
                } else if (!checkDni2) {
                    showError(R.string.accept)
                } else {
                    showAlert()
                }
            }
        }
    }


    //Funció que crea l'alert de tipus AlertDialog que es mostrarà si el registre no ha estat un èxit
    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.error)
        builder.setMessage(R.string.signup_failed)
        builder.setPositiveButton(R.string.accept, null)
        builder.show()
    }


    //Funcio que mostra el resultat del registre si ha tingut exit, mitjançant la pantalla Home
    private suspend fun showSucces(email: String, proveidor: tipusProveidor, dni: String) {
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
            binding.etxtRegMail.text.toString(),
            binding.etxtRegCognom.text.toString(),
            binding.etxtRegTel.text.toString()
        )
        startActivity(homeIntent)

        enviarNotificacio()

    }

    //Funcion para comprobar el apellido
    private fun checkLastName(name: String): Boolean {
        val checkLastName = "^[\\p{L}\\s.’\\-,]+\$".toRegex()
        var checker = false
        if (checkLastName.matches(name)) checker = true
        return checker
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

        var comprobacion: Boolean = false
        val dniNum = dni.substring(0, dni.length - 1)
        if (dni.length < 9 || !dniNum.isDigitsOnly()) {
            return comprobacion
        }
        val dniLletra = dni.substring(dni.length - 1).uppercase()
        val letraDni = "TRWAGMYFPDXBNJZSQVHLCKE"
        if (dniLletra == letraDni[dniNum.toInt() % 23].toString()) comprobacion = true
        return comprobacion
    }

    private suspend fun checkDni2(dni: String): Boolean {
        return db.collection("users").whereEqualTo("DNI", dni)
            .get()
            .await().isEmpty
    }

    private fun test() {
        if (!checkDni(binding.etxtRegDni.text.toString())) {
            binding.etxtRegDni.error = R.string.invalid_dni.toString()
        }
    }

    private fun showError(error: Int) {
        val errorDis = AlertDialog.Builder(this)
        errorDis.setTitle(R.string.login_failed)
        errorDis.setMessage(error)
        errorDis.setPositiveButton(R.string.accept, null)
        errorDis.show()
    }

    private fun creacioCanalNotificacio() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //>=26 version Oreo i superiors
            val nom = "Titol de la notificació"
            val descripcio = "Descripció notificació."
            val importancia = NotificationManager.IMPORTANCE_DEFAULT
            val canal = NotificationChannel(CHANNEL_ID, nom, importancia)
            canal.description = descripcio
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(canal)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun enviarNotificacio() {

        //Activity que es mostrarà al fer click a la notificació
        val resultIntent: Intent = Intent(this, ConRegistro::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val resultPendingIntent = PendingIntent.getActivity(
            this, 0, resultIntent, 0
        )

        val bitmapGran =
            BitmapFactory.decodeResource(applicationContext.resources, R.drawable.notificacio)

        val textMessage = getString(R.string.new_message)
        val regSuccess = getString(R.string.register_success)
        val contMessage = getString(R.string.register_complete)

        //Construim la notificació
        val mBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentTitle(regSuccess)
            .setContentText(textMessage)
            .setLargeIcon(bitmapGran)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contMessage))
            .setContentIntent(resultPendingIntent)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)


        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(notificacioId, mBuilder.build())

    }

}
