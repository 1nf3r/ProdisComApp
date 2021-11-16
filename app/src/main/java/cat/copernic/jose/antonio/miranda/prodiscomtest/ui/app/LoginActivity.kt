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
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.*


class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var usuario: String
    private val db = FirebaseFirestore.getInstance()
    private val auth:FirebaseAuth = Firebase.auth
    private var loginComplete = "Error"
   // private var displayName = ""

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
            login.isEnabled = loginState.isDataValid

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

            checkUser(binding.username.text.toString())
//Coroutines
            if (loginResult.error != null && loginComplete == "Error") {
                Log.d("TAG","No Entras")
                showLoginFailed(loginResult.error!!)
            }else if (loginResult.success != null && loginComplete == "Hecho") {
                Log.d("TAG","Entras")
                updateUiWithUser(loginResult.success!!)
            }
            Log.d("TAG","XD Entras")

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

            login.setOnClickListener {
                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }

    }



    private fun updateUiWithUser(model: LoggedInUserView) {

        getName()
        /*val displayName =getName()
        val welcome = getString(R.string.welcome)
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()*/

        //Complete and destroy login activity once successful
        intent = Intent(applicationContext, MainActivity::class.java)
        //intent.putExtra("DNI",username.text.toString())
        startActivity(intent)
        finish()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }



    fun checkUser(dni:String)  = runBlocking<Unit> {
        val getUserInfo = db.collection("users").document(dni)
        getUserInfo.get()
            .addOnSuccessListener { document ->
                if (document != null && document.getField<String>("email") != null) {
                    //Log.d("TAG", "Email: ${document.id} => ${document.data}")
                    login(document.getField<String>("email")!!)
                } else {
                    Log.d("TAG", "No such document")
                    login("aadkshjsa@fgdyuj.kgjsdgsk")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
    }

    private fun login(email:String){
        auth.signInWithEmailAndPassword(email, binding.password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                   // Log.d("TAG", "signInWithEmail:success")
                    loginComplete = "Hecho"

                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    loginComplete = "Error"
                    //updateUI(null)
                }

            }

    }

    private fun getName(){
        val currentUser = auth.currentUser?.email
        //displayName = "F"
        val getUserInfo = db.collection("users").whereEqualTo("email",currentUser)
        getUserInfo.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    //Log.d("TAG", "Nombre: ${document.id} => ${document.data}")
                    val displayName = document.getField<String>("Nombre")!!
                    val welcome = getString(R.string.welcome)
                    Toast.makeText(
                        applicationContext,
                        "$welcome $displayName",
                        Toast.LENGTH_LONG
                    ).show()
                //return@addOnSuccessListener
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }

        //delay(5000)
        //return "F"
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

