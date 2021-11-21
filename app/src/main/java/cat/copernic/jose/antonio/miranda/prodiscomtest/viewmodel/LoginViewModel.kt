package cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cat.copernic.jose.antonio.miranda.prodiscomtest.data.UserFormData
import cat.copernic.jose.antonio.miranda.prodiscomtest.util.ErrorLogs
import cat.copernic.jose.antonio.miranda.prodiscomtest.util.firebasedb.LoginDb

class LoginViewModel : ViewModel() {

    val _LoginData = MutableLiveData<UserFormData>()
    val LoginData: LiveData<UserFormData> get() = _LoginData
    val loginDb: LoginDb = LoginDb()
    val showError: ErrorLogs = ErrorLogs()

    fun userLogin(activity: Activity) {
        if (loginDb.searchByDni()) {
            showError.showError(
                activity,
                "Failed Login",
                "Dni o contrasenya incorrectes"
            )
        } else {
            loginDb.loginWithEmail(loginDb.getMail())
        }

    }
}