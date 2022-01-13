package cat.copernic.jose.antonio.miranda.prodiscomtest.ui

import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.ActivityLoginBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.register.Register
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user_n.MainActivityUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*


class LoginActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityLoginBinding
    private var currentUser = Firebase.auth.currentUser
    var checkAdmin = false
    private var validate: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(1000)
        setTheme(R.style.Theme_ProdisComTest)
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //viewModel = ViewModelProviders
        val login = binding.login
        val btnshow = binding.btnShow


        //Funcion para mostrar o ocultar la contrasenya en el login
        binding.btnShow?.setOnClickListener {
            if (binding.password.inputType == 1) {
                binding.password.inputType =
                    InputType.TYPE_NUMBER_VARIATION_PASSWORD or InputType.TYPE_CLASS_NUMBER
                btnshow?.setImageResource(R.drawable.ic_baseline_eye)
            } else {
                binding.password.inputType = 1
                btnshow?.setImageResource(R.drawable.ic_baseline_noeyexd)
            }

        }

        //Al clicar en necesitas ayuda te sale un alert indicando el mensaje.
        val builder = AlertDialog.Builder(this)
        binding.txtVAjuda?.setOnClickListener {
            builder.setTitle(R.string.help)
            builder.setMessage(R.string.login_help)
            builder.setPositiveButton(R.string.accept, null)
            builder.show()
        }

        //Al clicar te lleva a recuperar contrasenya
        binding.txtVOlCont?.setOnClickListener {
            startActivity(Intent(this, RestorePassword::class.java))
        }

        //Al clicar en Registrarse te lleva al activity Register.
        binding.txtVRegister?.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        binding.btnCat?.setOnClickListener{
            val localizacion = Locale("ca", "ES")
            Locale.setDefault(localizacion)
            val config = Configuration()
            config.locale = localizacion
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        }

        binding.btnEsp?.setOnClickListener {
            val localizacion = Locale("es", "ES")
            Locale.setDefault(localizacion)
            val config = Configuration()
            config.locale = localizacion
            baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
        }
        CoroutineScope(Dispatchers.Main).launch {

            if (currentUser != null) {
                darkMode()
                checkAdmin()
                if(checkAdmin) {
                    intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else {
                    intent = Intent(applicationContext, MainActivityUser::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                login.setOnClickListener {
                    login.isEnabled = false
                    login.isClickable = false
                    CoroutineScope(Dispatchers.Main).launch {
                        if (binding.username.text.toString().isNotEmpty()
                            && binding.password.text.toString().isNotEmpty()
                        ) {
                            db.collection("users")
                                .whereEqualTo(
                                    "DNI",
                                    binding.username.text.toString().uppercase()
                                )
                                .get()
                                .addOnSuccessListener { documents ->
                                    if (documents.isEmpty) {
                                        showLoginError()
                                    } else {
                                        for (document in documents) {
                                            CoroutineScope(Dispatchers.Main).launch {
                                                loginWithEmail(document.get("email") as String)

                                            }
                                        }
                                    }

                                }.await()
                        } else {
                            showLoginError()
                        }
                    }
                }
            }
        }
    }

    suspend fun loginWithEmail(email: String) {
        if (checkBooleans(email)) {
            if (checkAdmin) {
                var realPass = "prodis"
                realPass += binding.password.text.toString()
                Firebase.auth.signInWithEmailAndPassword(email, realPass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            showLoginError()
                        }
                    }
            } else {
                var realPass = "prodis"
                realPass += binding.password.text.toString()
                Firebase.auth.signInWithEmailAndPassword(email, realPass)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            intent = Intent(applicationContext, MainActivityUser::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            showLoginError()
                        }
                    }
            }
        }
    }

    private fun showLoginError() {
        binding.login.isEnabled = true
        binding.login.isClickable = true
        val errorDis = AlertDialog.Builder(this)
        errorDis.setTitle(R.string.failed_login)
        errorDis.setMessage(R.string.invalid_auth)
        errorDis.setPositiveButton(R.string.accept, null)
        errorDis.show()
    }

    private suspend fun checkBooleans(email: String): Boolean {
        var checkBloqueado = true
        var checkEliminado = true
        var checkValidado = false
        var check = false
        db.collection("users").document(email)
            .get()
            .addOnSuccessListener { document ->
                checkAdmin = document.get("zAdmin") as Boolean
                checkBloqueado = document.get("zBloqueado") as Boolean
                checkEliminado = document.get("zEliminado") as Boolean
                checkValidado = document.get("zValidado") as Boolean
            }.await()
        if (!checkBloqueado &&
            !checkEliminado &&
            checkValidado
        ) {
            check = true
            Log.i("Accedes", check.toString())
        } else if (!checkValidado) {
            enableButtons()
            Toast.makeText(this, R.string.user_not_validate, Toast.LENGTH_LONG).show()
        } else if (checkBloqueado) {
            enableButtons()
            Toast.makeText(this, R.string.user_block, Toast.LENGTH_LONG).show()
        } else if (checkEliminado) {
            enableButtons()
            Toast.makeText(this, R.string.user_deleted, Toast.LENGTH_LONG).show()
        } else {
            enableButtons()
            Toast.makeText(this, R.string.error_check_user, Toast.LENGTH_LONG).show()
        }
        return check
    }

    private suspend fun checkAdmin(){
        db.collection("users").document(currentUser?.email!!)
            .get()
            .addOnSuccessListener { document ->
                checkAdmin = document.get("zAdmin") as Boolean
            }.await()
    }

    private fun enableButtons() {
        binding.login.isEnabled = true
        binding.login.isClickable = true
    }

    private fun darkMode() {
        val currentNightMode =
            resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        when (currentNightMode) {
            Configuration.UI_MODE_NIGHT_YES -> {
                this.setTheme(R.style.DarkTheme)

            }
            Configuration.UI_MODE_NIGHT_NO -> {
                this.setTheme(R.style.Theme_ProdisComTest)
            }
        }
    }
}