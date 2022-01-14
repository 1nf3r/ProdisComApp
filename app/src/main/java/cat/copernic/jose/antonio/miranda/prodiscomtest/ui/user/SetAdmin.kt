package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentSetAdminBinding
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class SetAdmin : Fragment() {
    private var _binding: FragmentSetAdminBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private lateinit var getUserInfo: DocumentReference
    private var found = false
    private var isAdmin = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSetAdminBinding.inflate(inflater, container, false)

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
            if (!found){
                Toast.makeText(requireContext(), R.string.user_not_found,Toast.LENGTH_LONG).show()
            } else {
                confirmUpdate()
            }
        }
        return binding.root
    }

    private fun getInfo(mailUser: String): Boolean {
        found = false
        getUserInfo = db.collection("users").document(mailUser)
        getUserInfo.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    printInfo(
                        document.get("email") as String,
                        document.get("zAdmin") as Boolean
                    )
                    found = true
                } else {
                    notFoundError()
                }
            }
        return found
    }


    private fun printInfo(mail: String, admin: Boolean) {
        binding.tVMailResult.setText(mail)
        isAdmin = admin
        if (admin) {
            binding.tVAdmin.setText("Si")
        } else {
            binding.tVAdmin.setText("No")

        }

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
        if (isAdmin) {
            isAdmin = false
        } else {
            isAdmin = true
        }
        getUserInfo.update(
            "zAdmin",
            isAdmin
        )
        isAdmin = false
    }

    private val positiveButtonClick = { dialog: DialogInterface, which: Int -> updateUser() }

    private fun confirmUpdate() {
        val updateDis = AlertDialog.Builder(activity)
        updateDis.setTitle(R.string.modificar_usuari)
        updateDis.setMessage(R.string.mod_confirm)
        updateDis.setPositiveButton(
            R.string.accept,
            DialogInterface.OnClickListener(function = positiveButtonClick)
        )
        updateDis.setNegativeButton(R.string.cancel, null)
        updateDis.show()
    }
}

