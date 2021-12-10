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
            builder.setTitle(R.string.help)
            builder.setMessage(
               R.string.help_login_info
            )
            builder.setPositiveButton(R.string.accept, null)
            builder.show()
        }

        //Al clicar en Registrarse te lleva al activity Register.
        binding.txtVRegister?.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        //Al clicar se iniciara el proceso de login
        login.setOnClickListener {
            login.isEnabled = false
            login.isClickable = false
            CoroutineScope(Dispatchers.Main).launch {
                if (binding.username.text.toString().isNotEmpty()
                    && binding.password.text.toString().isNotEmpty()
                ) {
                        db.collection("users").whereEqualTo("DNI", binding.username.text.toString())
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
        if(checkValidated(email)) {
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
        }else {
            binding.login.isEnabled = true
            binding.login.isClickable = true
            Toast.makeText(this, R.string.user_not_validate, Toast.LENGTH_LONG).show()
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

    suspend fun checkValidated(email : String): Boolean{
        var check = false
        db.collection("users").document(email)
            .get()
            .addOnSuccessListener { document ->
                check = document.get("zValidado") as Boolean
                Log.i("Validado",check.toString())
            }.await()
        return check
    }

}


