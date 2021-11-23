package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentDelUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class delUser : Fragment() {
    private var _binding: FragmentDelUserBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private lateinit var getUserInfo: DocumentReference
    private var found = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDelUserBinding.inflate(inflater, container, false)

        binding.btnReturnDelUser.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.usuarios,
                null
            )
        )
        binding.btnDelUserToHome.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.menu_principal,
                null
            )
        )

        binding.iVBuscar.setOnClickListener {
            if (binding.eTxtSearch.text.toString().isNotEmpty()) {
                getInfo(binding.eTxtSearch.text.toString())
            } else {
                notFoundError()
            }
        }

        binding.btnDelUs.setOnClickListener {
            if (found) {
                conDelUser()
            } else {
                notFoundError()
            }
        }

        return binding.root
    }

    //TODO USAR LA FUNCION DE COMPROBACION PARA DNI Y MAIL PARA BUSCAR POR LOS DOS CAMPOS

    private fun getInfo(mailUser: String): Boolean {
        found = false
        getUserInfo = db.collection("users").document(mailUser)
        getUserInfo.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    printInfo(
                        document.get("email") as String,
                        document.get("Nombre") as String,
                        document.get("DNI") as String
                    )
                    found = true
                } else {
                    notFoundError()
                }
            }
        return found
    }

    private fun printInfo(mail: String, nom: String, dni: String) {
        binding.txResultMail.text = mail
        binding.txResultNom.text = nom
        binding.txResultDni.text = dni
    }

    private fun notFoundError() {
        val errorDis = AlertDialog.Builder(activity)
        errorDis.setTitle("Usuari no trovat")
        errorDis.setMessage("No existeix cap usuari amb aquestes dades")
        errorDis.setPositiveButton("Aceptar", null)
        errorDis.show()
    }

    private fun delUser() {
        getUserInfo.delete()
        found = false
    }

    private fun conDelUser() {
        val delDis = AlertDialog.Builder(activity)
        delDis.setTitle("Eliminar Usuari")
        delDis.setMessage("Estas segur que vols eliminar l'usuari? ")
        delDis.setPositiveButton(
            "Confirmar",
            DialogInterface.OnClickListener(function = positiveButtonClick)
        )
        delDis.setNegativeButton("Cancelar", null)
        delDis.show()
    }

    private val positiveButtonClick = { dialog: DialogInterface, which: Int -> delUser() }
}