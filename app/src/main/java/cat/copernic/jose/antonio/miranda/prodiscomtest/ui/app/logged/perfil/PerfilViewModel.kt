package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.logged.perfil

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis


class PerfilViewModel: ViewModel() {


    private val db = FirebaseFirestore.getInstance()

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

    suspend fun getInfo2() = runBlocking{
         _nombre.value = "F"
         _correo.value = "F"
         _nacimiento.value = "F"
        val getUserInfo = db.collection("users").whereEqualTo("DNI","12345678A")
        coroutineScope {
            //delay(3000L)

            val getUsuario = launch {
                getUserInfo.get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            Log.d("TAG", "Email: ${document.id} => ${document.data}")
                            _nombre.value = document.getField<String>("Nombre")!!
                            _correo.value = document.getField<String>("email")!!
                            _nacimiento.value = document.getField<String>("informacion")!!

                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w("TAG", "Error getting documents: ", exception)
                    }
            }


        }

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



}