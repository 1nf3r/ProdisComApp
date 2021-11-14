package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.register

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.ActivityRegisterBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.LoginActivity
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.logged.perfil.PerfilViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Register : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
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

        //Al clicar te lleva a la pantalla de confirmacion de registro
        /*binding.btnRegistro.setOnClickListener {
            startActivity(Intent(this, ConRegistro::class.java))
            finish()
        }*/
        //Setup
        setup()

    }

    private fun setup() {

        //title="Autenticació" //Nom pantalla

        //Accedim al botó de registrar-se i escoltem l'esdevniment
        binding.btnRegistro.setOnClickListener {

            //Si s'han introduit el correu i contrasenya
            if (binding.etxtRegMail.text.isNotEmpty() && binding.etxtRegCont.text.isNotEmpty() ) { //Creem el registre amb email i contrasenya...

                //Registrem a l'usuari i amb el mètode addOnCompleteListener, ens notificarà si el registre a estat un èxit o no.
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.etxtRegMail.text.toString(),
                    binding.etxtRegCont.text.toString()
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
        /*val objectAlerDialog = androidx.appcompat.app.AlertDialog.Builder(this)
        objectAlerDialog.setTitle("ERROR")
        objectAlerDialog.setMessage("No s'ha pogut crear el registre")
        objectAlerDialog.setPositiveButton("Acceptar", null)
        var alertDialog: androidx.appcompat.app.AlertDialog = objectAlerDialog.create()
        alertDialog.show()*/

    }


    //Funcio que mostra el resultat del registre si ha tingut exit, mitjançant la pantalla Home
    private fun showSucces(email: String, proveidor: tipusProveidor) {
        //Creem un objecte Intent passant-li com a paràmetre el context de l'Activitat acual i el nom de la pantalla a la que volem navegar, és a dir, HomeActivity
        val homeIntent: Intent = Intent(this, ConRegistro::class.java).apply {
            putExtra("email", email) //Correu a mostrar
            putExtra(
                "proveidor",
                proveidor.name
            ) //proveidor a mostra. En el nostre cas de moment, només BASIC
        }

        // Create a new user with a first and last name
        viewModel.saveDB(
            binding.etxtRegNom.text.toString(),
            binding.etxtRegDni.text.toString(),
            binding.etxtRegMail.text.toString(),
            binding.etxtRegCont.text.toString(),
            binding.editTextTextPersonName5.text.toString()
        )
        startActivity(homeIntent)

    }

    /* private fun comprobar() {
          if (!comprobarMail(binding.txtRegMail.text.toString())) {
              binding.txtRegMail.error =
                  "Mail no valid" //Muesta el error dentro del input text con el icono rojo
              //binding.btnRegistro.isEnabled = false //desactiva el boton de registro
          }


      }*/

    /*private fun setup() {
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
    }*/
}
