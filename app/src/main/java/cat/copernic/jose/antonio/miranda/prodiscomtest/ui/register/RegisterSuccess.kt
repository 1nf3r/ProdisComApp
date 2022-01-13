package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.ActivityConRegistroBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentMenuPrincipalBinding
import com.google.firebase.auth.FirebaseAuth

enum class tipusProveidor() {
    BASIC //Mètode de registre email i contrasenya.
}

class ConRegistro : AppCompatActivity() {
    private lateinit var binding: ActivityConRegistroBinding // Binding per vincular el HomeActivity amb el Layout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*Setup*/
        //Recuperació de les dades del registre o autenticació
        val bundle: Bundle? = intent.extras
        //Poden ser nuls, ja que si no hem fet un registre o una autenticació, no existiran
        val email: String? = bundle?.getString("email")
        val proveidor: String? = bundle?.getString("proveidor")
        val dni: String? = bundle?.getString("dni")

        setup(email ?: "", proveidor ?: "", dni?: "") //Si no existeixen es mostrarà buit
        FirebaseAuth.getInstance().signOut()
        binding.btnToInici.setOnClickListener {
            finish()
        }
    }

    //Mostrem la pantalla amb el correu de l'autenticació o registre i el proveidor, que en aquest cas serà basic
    private fun setup(email: String, proveidor: String, dni: String) {
        title = "Inici"

        binding.txtMail.text = email
        binding.txtDni.text = dni


    }

}