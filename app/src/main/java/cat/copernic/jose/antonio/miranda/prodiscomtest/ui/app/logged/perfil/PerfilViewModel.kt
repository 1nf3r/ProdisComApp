package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.logged.perfil

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.sql.Timestamp


class PerfilViewModel: ViewModel() {

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String>
        get() = _nombre
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
        _nombre.value = "Juan"//TODO("Coger el nombre de la base de datos")
        _correo.value =  "Juan" //TODO("Coger el correo de la base de datos")
        _telefono.value =  0//TODO("Coger el teléfono de la base de datos")
        _nacimiento.value =  "04/20/1969"//TODO("Coger el nacimiento de la base de datos")
        Log.i("GameViewModel", "GameViewModel created!")
    }

    fun setInfo(nombre:String, correo:String, telefono:Int, nacimiento:String){
        _nombre.value = nombre
        _correo.value = correo
        _telefono.value = telefono
        _nacimiento.value = nacimiento


    }


}