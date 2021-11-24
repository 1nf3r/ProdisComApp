package cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel

import android.app.Activity
import android.app.AlertDialog
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class DelUserViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var getUserInfo: DocumentReference
    private lateinit var activity : Activity
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

    /*private val _telefono = MutableLiveData<Int>()
    val telefono: LiveData<Int>
        get() = _telefono
    private val _nacimiento = MutableLiveData<String>()
    val nacimiento: LiveData<String>
        get() = _nacimiento*/

    init {
        //getInfo()

        Log.d("TAG", "GameViewModel created!")
    }
//TODO USAR LA FUNCION DE COMPROBACION PARA DNI Y MAIL PARA BUSCAR POR LOS DOS CAMPOS

    suspend fun getInfo(mailUser: String, activityL : Activity): Boolean {
        activity = activityL
        found = false
        getUserInfo = db.collection("users").document(mailUser)
        getUserInfo.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    saveInfo(
                        document.get("email") as String,
                        document.get("Nombre") as String,
                        document.get("DNI") as String
                    )
                    found = true
                    Log.d("DelUser", found.toString())
                } else {
                    notFoundError()
                }
            }.await()
        Log.d("DelUser", found.toString())
        return found
    }

    private fun saveInfo(mail: String, nom: String, dni: String) {
        _correo.value = mail
        _nombre.value = nom
        _dni.value = dni
    }

    fun notFoundError() {
        val errorDis = AlertDialog.Builder(activity)
        errorDis.setTitle("Usuari no trovat")
        errorDis.setMessage("No existeix cap usuari amb aquestes dades")
        errorDis.setPositiveButton("Aceptar", null)
        errorDis.show()
    }

    fun delUser() {
        getUserInfo.delete()
        var user = FirebaseAuth.getInstance().currentUser!!
        user.delete()
        found = false
    }


}