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
        if (!comprobarMail(binding.txtRegMail.text.toString())) {
            binding.txtRegMail.error =
                "Mail no valid" //Muesta el error dentro del input text con el icono rojo
            //binding.btnRegistro.isEnabled = false //desactiva el boton de registro
        }


    }

    private fun setup() {
        val nom = binding.txtRegNom.text.toString()
        val dni = binding.txtRegDni.text.toString()
        val email = binding.txtRegMail.text.toString()
        val password = binding.txtRegCont.text.toString()
        val confirmPass = binding.txtRegConfPass.text.toString()

        binding.btnRegistro.setOnClickListener {
            if (binding.txtRegMail.text.isNotEmpty() && binding.txtRegCont.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.txtRegMail.text.toString(),
                    binding.txtRegCont.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful) { //Si el registre ha estat un èxit...
                            showSucces(it.result?.user?.email ?: "", tipusProveidor.BASIC)
                            db.collection("usuaris")
                                .document(email)
                                .set(hashMapOf("DNI" to dni, "Nom" to nom))
                            finish()
                        } else { //Si el registre no ha estat un èxit...
                            showAlert()
                        }

                    }

            }
        }
    }


    private fun showAlert() {                   //Alert Error Registre
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(
            "No s'ha pogut completar el registre."
        )
        builder.setPositiveButton("Aceptar", null)
        builder.show()
    }


    private fun showSucces(email: String, proveidor: tipusProveidor) { //Registre amb exit
        val homeIntent: Intent = Intent(this, ConRegistro::class.java).apply {
            putExtra("email", email) //Correu a mostrar
            putExtra(
                "proveidor",
                proveidor.name
            ) //proveidor a mostra. En el nostre cas de moment, només BASIC
        }
    }

    private fun comprobarMail(email: String): Boolean {
        return email.contains("@")
    }


}
