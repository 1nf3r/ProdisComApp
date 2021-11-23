package cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class PerfilViewModel: ViewModel() {


    private val db = FirebaseFirestore.getInstance()
    private val currentUser = Firebase.auth.currentUser

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String>
        get() = _nombre

    private val _dni = MutableLiveData<String>()
    val dni: LiveData<String>
        get() = _dni

    private val _correo = MutableLiveData<String>()
    val correo: LiveData<String>
        get() = _correo
    private val _telefono = MutableLiveData<Int>()
    val telefono: LiveData<Int>
        get() = _telefono
    private val _nacimiento = MutableLiveData<String>()
    val nacimiento: LiveData<String>
        get() = _nacimiento


    init {
        //getInfo()

        Log.d("TAG", "GameViewModel created!")
    }

    fun setDni(dni:String){
        _dni.value = dni
        //_correo.value = correo
        //_telefono.value = telefono
        //_nacimiento.value = nacimiento
    }

     fun getInfo(){
        val getUserInfo = db.collection("users").document(currentUser?.email!!)
            getUserInfo.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        //Log.d("TAG", "Email: ${document.id} => ${document.data}")
                        _nombre.value = document.getField<String>("Nombre")!!
                        _correo.value = document.getField<String>("email")!!
                        _nacimiento.value = document.getField<String>("informacion")!!
                    } else {
                        Log.d("TAG", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("TAG", "Error getting documents: ", exception)
                }//.await()
        }
          //  }


        //}

        /*getUserInfo.get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d("TAG", "Nombre: ${document.id} => ${document.data}")
                    //Log.d("TAG", document.getField<String>("Nombre")!!)
                    _nombre.value = document.getField<String>("Nombre")
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }*/

    }