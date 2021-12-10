package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntegerRes
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentModUserBinding
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class modUser : Fragment() {
    private var _binding: FragmentModUserBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private lateinit var getUserInfo: DocumentReference
    private var found = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModUserBinding.inflate(inflater, container, false)

        binding.btnReturnModUser.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.usuarios,
                null
            )
        )
        binding.btnModUserToHome.setOnClickListener(
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

        binding.btnModUs.setOnClickListener {
            confirmUpdate()
        }
        return binding.root
    }

    //TODO USAR LA FUNCION DE COMPROBACION PARA DNI Y MAIL PARA BUSCAR POR LOS DOS CAMPOS
    //TODO PASAR A MVVM

    private fun getInfo(mailUser: String): Boolean {
        found = false
        getUserInfo = db.collection("users").document(mailUser)
        getUserInfo.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    printInfo(
                        document.get("email") as String,
                        document.get("Nombre") as String,
                        document.get("DNI") as String,
//                        document.get("Tel") as String
                    )
                    found = true
                } else {
                    notFoundError()
                }
            }
        return found
    }


    private fun printInfo(mail: String, nom: String, dni: String /*tel: String*/) {

        binding.txResultMail.setText(mail)
        binding.txResultNom.setText(nom)
        binding.txResultDni.setText(dni)
//        binding.txResultTel.setText(tel)

    }

    private fun notFoundError() {
        val errorDis = AlertDialog.Builder(activity)
        errorDis.setTitle(R.string.user_not_found)
        errorDis.setMessage(R.string.any_user_data)
        errorDis.setPositiveButton(R.string.accept, null)
        errorDis.show()
    }

    //HAY QUE PROBAR DE CAMBIAR EL MAIL PARA VER QUE PASA EN FIREBASE
    private fun updateUser() {
        getUserInfo.update(
            "email",
            binding.txResultMail.text.toString(),
            "DNI",
            binding.txResultDni.text.toString(),
            "Nombre", binding.txResultNom.text.toString()
        )
    }

    private val positiveButtonClick = { dialog: DialogInterface, which: Int -> updateUser() }

    private fun confirmUpdate() {
        val updateDis = AlertDialog.Builder(activity)
        updateDis.setTitle(R.string.modificar_usuari)
        updateDis.setMessage(R.string.mod_confirm)
        updateDis.setPositiveButton(
            Resources.getSystem().getString(R.string.accept),
            DialogInterface.OnClickListener(function = positiveButtonClick)
        )
        updateDis.setNegativeButton(R.string.cancel, null)
        updateDis.show()
    }
}

