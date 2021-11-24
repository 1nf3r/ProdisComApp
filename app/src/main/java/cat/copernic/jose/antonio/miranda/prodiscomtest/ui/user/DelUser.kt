package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentDelUserBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel.DelUserViewModel
import cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel.PerfilViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

class delUser : Fragment() {
    private lateinit var viewModel: DelUserViewModel
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

        viewModel = ViewModelProvider(this)[DelUserViewModel::class.java]
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
        CoroutineScope(Dispatchers.Main).launch {
            if (binding.eTxtSearch.text.toString().isNotEmpty()) {
                found = viewModel.getInfo(binding.eTxtSearch.text.toString(), requireActivity())
                Log.d("DelUser","Busca")
                printInfo()

            } else {
                notFoundError()
            }
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

    private fun printInfo() {
            Log.i("DelUser","Correo: "+viewModel.correo.value)
            Log.i("DelUser","Nombre: "+viewModel.nombre.value)
            Log.i("DelUser","Dni: "+viewModel.dni.value)
            binding.txResultMail.text = viewModel.correo.value
            binding.txResultNom.text = viewModel.nombre.value
            binding.txResultDni.text = viewModel.dni.value
    }

    private fun notFoundError() {
        val errorDis = AlertDialog.Builder(activity)
        errorDis.setTitle("Usuari no trovat")
        errorDis.setMessage("No existeix cap usuari amb aquestes dades")
        errorDis.setPositiveButton("Aceptar", null)
        errorDis.show()
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

    private val positiveButtonClick = { dialog: DialogInterface, which: Int -> viewModel.delUser() }
}