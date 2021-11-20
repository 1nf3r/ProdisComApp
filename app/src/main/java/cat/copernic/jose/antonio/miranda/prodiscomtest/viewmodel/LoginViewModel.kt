package cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.copernic.jose.antonio.miranda.prodiscomtest.data.UserFormData
import java.nio.file.attribute.UserDefinedFileAttributeView

class LoginViewModel : ViewModel() {

    val _LoginData = MutableLiveData<UserFormData>()
    val LoginData : LiveData<UserFormData> get() = _LoginData


    fun userLogin(){
        val currentUser: UserFormData = UserFormData()

    }
}