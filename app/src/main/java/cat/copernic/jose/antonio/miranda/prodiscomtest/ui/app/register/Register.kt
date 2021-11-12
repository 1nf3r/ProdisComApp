package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.register

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.ActivityRegisterBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    private fun comprobar() {
        if (!comprobarMail(binding.editTextTextEmailAddress.text.toString())) {
            binding.editTextTextEmailAddress.error =
                "Mail no valid" //Muesta el error dentro del input text con el icono rojo
            //binding.btnRegistro.isEnabled = false //desactiva el boton de registro
        }


    }

    private fun setup() {
        binding.btnRegistro.setOnClickListener {
            if (binding.editTextTextEmailAddress.text.isNotEmpty() && binding.editTextTextPassword2.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.editTextTextEmailAddress.text.toString(),
                    binding.editTextTextPassword2.text.toString()
                ).addOnCompleteListener {

                    if (it.isSuccessful) { //Si el registre ha estat un èxit...
                        showSucces(it.result?.user?.email ?: "", tipusProveidor.BASIC)
                        finish()
                    } else { //Si el registre no ha estat un èxit...
                        showAlert()
                    }

                }

            }
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

    private fun showSucces(email: String, proveidor: tipusProveidor) {
        val homeIntent: Intent = Intent(this, ConRegistro::class.java)
        startActivity(homeIntent)
    }

    private fun comprobarMail(email: String): Boolean {
        return email.contains("@")
    }


}
