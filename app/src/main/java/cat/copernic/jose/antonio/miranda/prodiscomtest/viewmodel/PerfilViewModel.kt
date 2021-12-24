package cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import androidx.fragment.app.FragmentActivity


class PerfilViewModel : ViewModel() {


    private val db = FirebaseFirestore.getInstance()
    private val currentUser = Firebase.auth.currentUser
    private lateinit var getUserInfo: DocumentReference
    private var found = false

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String>
        get() = _nombre

    private val _dni = MutableLiveData<String>()
    val dni: LiveData<String>
        get() = _dni

    private val _correo = MutableLiveData<String>()
    val correo: LiveData<String>
        get() = _correo
    private val _telefono = MutableLiveData<String>()
    val telefono: LiveData<String>
        get() = _telefono
    private val _nacimiento = MutableLiveData<String>()
    val nacimiento: LiveData<String>
        get() = _nacimiento



    fun setDni(dni: String) {
        _dni.value = dni
        //_correo.value = correo
        //_telefono.value = telefono
        //_nacimiento.value = nacimiento
    }

    fun getInfo(activity: FragmentActivity?): Boolean {
        found = false
        getUserInfo = db.collection("users").document(currentUser?.email!!)
        getUserInfo.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    printInfo(
                        document.get("email") as String,
                        document.get("nombre") as String,
                        document.get("DNI") as String,
                        document.get("Telefono") as String,
                        document.get("Fecha") as String
                    )
                    found = true
                } else {
                    notFoundError(activity)
                }
            }
        return found
    }


    private fun printInfo(mail: String, nom: String, dni: String, telf: String, birth: String) {

        _correo.value = mail
        _nombre.value = nom
        _dni.value = dni
        _telefono.value = telf
        _nacimiento.value = birth

    }

    private fun notFoundError(activity: FragmentActivity?) {
        val errorDis = AlertDialog.Builder(activity)
        errorDis.setTitle(R.string.user_not_found)
        errorDis.setMessage(R.string.any_user_data)
        errorDis.setPositiveButton(R.string.accept, null)
        errorDis.show()
    }

    private fun updateUser() {
//        getUserInfo.update(
//            "email",
//            binding.txResultMail.text.toString(),
//            "DNI",
//            binding.txResultDni.text.toString(),
//            "Nombre", binding.txResultNom.text.toString()
//        )
    }

    private val positiveButtonClick = { dialog: DialogInterface, which: Int -> updateUser() }

    private fun confirmUpdate(activity: FragmentActivity?) {
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