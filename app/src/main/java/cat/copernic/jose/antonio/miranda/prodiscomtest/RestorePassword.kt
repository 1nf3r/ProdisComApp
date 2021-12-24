package cat.copernic.jose.antonio.miranda.prodiscomtest

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.ActivityRestorePasswordBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class RestorePassword : AppCompatActivity() {
    private lateinit var binding: ActivityRestorePasswordBinding
    private val db = FirebaseFirestore.getInstance()
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRestorePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        //Boto de retornar a la pantalla de login
        binding.btnReturnMain.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        //Boto d'enviar
        binding.send.setOnClickListener {
            if (binding.dni.text.isEmpty() || binding.email.text.isEmpty()) {
                Toast.makeText(this, R.string.field_empty, Toast.LENGTH_LONG).show()
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    if (binding.dni.text.toString().isNotEmpty()
                        && binding.email.text.toString().isNotEmpty()
                    ) {
                        db.collection("users")
                            .whereEqualTo("DNI", binding.dni.text.toString().uppercase())
                            .get()
                            .addOnSuccessListener { documents ->
                                if (documents.isEmpty) {
                                    showLoginError()
                                } else {
                                    for (document in documents) {
                                        CoroutineScope(Dispatchers.Main).launch {
                                            resetPass(document.get("email") as String)
                                        }
                                    }
                                }

                            }.await()
                    } else {
                        showLoginError()
                    }
                }
            }


        }
    }

    private fun showLoginError() {
        val errorDis = AlertDialog.Builder(this)
        errorDis.setTitle(R.string.error_check_user)
        errorDis.setMessage(R.string.not_match)
        errorDis.setPositiveButton(R.string.accept, null)
        errorDis.show()
    }

    private fun resetPass(mail: String){
        mAuth!!.sendPasswordResetEmail(mail).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, R.string.mail_success, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, R.string.mail_failed, Toast.LENGTH_LONG).show()
            }
        }
    }

}