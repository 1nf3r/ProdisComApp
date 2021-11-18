package cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.copernic.jose.antonio.miranda.prodiscomtest.data.UserFormData

class LoginViewModel : ViewModel() {

    val LoginData = MutableLiveData<UserFormData>()

    fun userLogin(){
        val currentUser: UserFormData = UserFormData()


    }
}