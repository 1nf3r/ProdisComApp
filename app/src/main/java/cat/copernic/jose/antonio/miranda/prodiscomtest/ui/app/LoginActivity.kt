package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.ActivityLoginBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.logged.MainActivity
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.register.Register
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var usuario: String
    private val db = FirebaseFirestore.getInstance()
    private var loginComplete = "Error"
    private var emailD: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.sleep(1000)
        setTheme(R.style.Theme_ProdisComTest)
        super.onCreate(savedInstanceState)



        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = binding.username
        val password = binding.password
        val login = binding.login
        val loading = binding.loading
        val btnshow = binding.btnShow


        //Funcion para mostrar o ocultar la contrasenya en el login
        binding.btnShow?.setOnClickListener {
            if (binding.password.inputType == 1) {
                binding.password.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
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

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            //login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer
            usuario = username.text.toString()
            loading.visibility = View.GONE

            //checkUser(binding.username.text.toString())


            if (loginResult.error != null && loginComplete == "Error") {
                Log.d("TAG", "No Entras")
                showLoginFailed(loginResult.error!!)
            } else if (loginResult.success != null && loginComplete == "Hecho") {
                Log.d("TAG", "Entras")
                // updateUiWithUser(loginResult.success!!)
            }
            Log.d("TAG", "XD Entras")

            setResult(Activity.RESULT_OK)


        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            /*login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }*/

        }


        //No funciona el failure, preguntar como hacer el error rojo en los formularios.
        login.setOnClickListener {
            if (username.text.toString().isNotEmpty() && password.text.toString().isNotEmpty()) {
                db.collection("users").whereEqualTo("DNI", username.text.toString())
                    .get()
                    .addOnSuccessListener { documents ->
                        //Log.d("TAG1", "Si funciona2")
                        for (document in documents) {
                            //Log.d("TAG1", "${document.id} => ${document.data}")
                            loginWithEmail(document.getString("email").toString())
                        }
                    }
                    .addOnFailureListener { exception ->
                        showError()
                    }
            } else {
                showError()
            }
        }

    }

    private fun loginWithEmail(email: String) {
        var realPass = "Prodis"
        realPass += binding.password.text.toString()
        Firebase.auth.signInWithEmailAndPassword(email, realPass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    showError()
                }
            }

    }

    private fun showError() {
        val errorDis = AlertDialog.Builder(this)
        errorDis.setTitle("Login Failed")
        errorDis.setMessage("DNI o Contrasenya incorrectes!!!")
        errorDis.setPositiveButton("Aceptar", null)
        errorDis.show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }

}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

