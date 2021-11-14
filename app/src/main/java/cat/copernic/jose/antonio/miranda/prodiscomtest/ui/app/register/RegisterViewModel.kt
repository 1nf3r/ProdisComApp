package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewModel: ViewModel() {

    private val db = FirebaseFirestore.getInstance()


    fun saveDB(nombre:String, dni:String,email:String,/*contraseña:String,*/info:String) {


        val users = db.collection("users")
        val userInfo = hashMapOf(
            "Nombre" to nombre,
            "DNI" to dni,
            "email" to email,
            //"Contraseña" to contraseña,
            "informacion" to info
        )
        users.document(dni).set(userInfo)
            //.addOnSuccessListener { documentReference ->
                //Log.d("Register", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
    }