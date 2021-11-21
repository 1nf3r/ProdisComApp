package cat.copernic.jose.antonio.miranda.prodiscomtest.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
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


class LoginActivity : AppCompatActivity() {


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
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_NUMBER_VARIATION_PASSWORD
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


        //TEST LOGIN//
        login.setOnClickListener {
            viewModel.userLogin(this, binding.username.text.toString() ,binding.password.text.toString())
        }

       /* //Al clicar se iniciara el proceso de login
        login.setOnClickListener {
            if (dni.text.toString().isNotEmpty() && password.text.toString().isNotEmpty()) {
                db.collection("users").whereEqualTo("DNI", dni.text.toString())
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            showLoginError()
                        } else {
                            for (document in documents) {
                                loginWithEmail(document.getString("email").toString())
                            }
                        }

                    }
            } else {
                showLoginError()
            }
        }*/

    }

   /* private fun loginWithEmail(email: String) {
        var realPass = "Prodis"
        realPass += binding.password.text.toString()
        Firebase.auth.signInWithEmailAndPassword(email, realPass)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
    }

    private fun showLoginError() {
        val errorDis = AlertDialog.Builder(this)
        errorDis.setTitle("Inici de Sessió fallat")
        errorDis.setMessage("DNI o Contrasenya incorrectes!!!")
        errorDis.setPositiveButton("Aceptar", null)
        errorDis.show()
    }*/


}


