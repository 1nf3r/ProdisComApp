package cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.copernic.jose.antonio.miranda.prodiscomtest.data.UserFormData
import cat.copernic.jose.antonio.miranda.prodiscomtest.util.ErrorLogs
import cat.copernic.jose.antonio.miranda.prodiscomtest.util.firebasedb.LoginDb

class LoginViewModel : ViewModel() {

    val _loginData = MutableLiveData<UserFormData>()
    val loginData: LiveData<UserFormData> get() = _loginData
    val loginDb: LoginDb = LoginDb()
    val showError: ErrorLogs = ErrorLogs()

    fun userLogin(activity: Activity, dni: String, passwd: String) {
        if (loginDb.searchByDni(dni, passwd)) {
            showError.showError(
                activity,
                "Failed Login",
                "Dni o contrasenya incorrectes"
            )

        }

    }
}