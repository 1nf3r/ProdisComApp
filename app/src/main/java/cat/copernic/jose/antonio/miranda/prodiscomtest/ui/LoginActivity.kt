package cat.copernic.jose.antonio.miranda.prodiscomtest.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.data.UserFormData
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.ActivityLoginBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.register.Register
import cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel.LoginViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class LoginActivity : AppCompatActivity() {


    private val db = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    var checkAdmin = false
    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(1000)
        setTheme(R.style.Theme_ProdisComTest)
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //viewModel = ViewModelProviders
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]


        val dni = binding.username.text.toString()
        val password = binding.password.text.toString()
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
            builder.setTitle("Ajuda")
            builder.setMessage(
                "Hauras d'introduir el DNI i la contrasenya de 4 digits, si no tens" +
                        " un usuari clica en el text Registrar-se."
            )
            builder.setPositiveButton("Aceptar", null)
            builder.show()
        }

        //Al clicar en Registrarse te lleva al activity Register.
        binding.txtVRegister?.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }


        /*  //TEST LOGIN//
          login.setOnClickListener {
              viewModel.userLogin(this, binding.username.text.toString() ,binding.password.text.toString())
          }*/

        //Al clicar se iniciara el proceso de login
        login.setOnClickListener {
            login.isEnabled = false
            login.isClickable = false
            CoroutineScope(Dispatchers.Main).launch {
                if (binding.username.text.toString().isNotEmpty()
                    && binding.password.text.toString().isNotEmpty()
                ) {
                    db.collection("users")
                        .whereEqualTo("DNI", binding.username.text.toString().uppercase())
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

    suspend fun loginWithEmail(email: String) {
        if (checkBooleans(email)) {
            if (checkAdmin) {
                var realPass = "Prodis"
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
                var realPass = "Prodis"
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
        errorDis.setTitle("Inici de Sessió fallat")
        errorDis.setMessage("DNI o Contrasenya incorrectes!!!")
        errorDis.setPositiveButton("Aceptar", null)
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
                Log.i("Validado", "Admin " + checkAdmin.toString())
                Log.i("Validado", "Bloqueado " + checkBloqueado.toString())
                Log.i("Validado", "Eliminado " + checkEliminado.toString())
                Log.i("Validado", "Validado " + checkValidado.toString())

            }.await()
        if (!checkBloqueado &&
            !checkEliminado &&
            checkValidado
        ) {
            check = true
            Log.i("Accedes", check.toString())
        } else if (!checkValidado) {
            enableButtons()
            Toast.makeText(this, "Usuari no validat", Toast.LENGTH_LONG).show()
        } else if (checkBloqueado) {
            enableButtons()
            Toast.makeText(this, "Usuari bloquejat", Toast.LENGTH_LONG).show()
        } else if (checkEliminado) {
            enableButtons()
            Toast.makeText(this, "Usuari eliminat", Toast.LENGTH_LONG).show()
        } else {
            enableButtons()
            Toast.makeText(this, "Error al comprovar usuari", Toast.LENGTH_LONG).show()
        }
        return check
    }

    private fun enableButtons() {
        binding.login.isEnabled = true
        binding.login.isClickable = true
    }

}


