package cat.copernic.jose.antonio.miranda.prodiscomtest

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.ActivityRestorePasswordBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.LoginActivity

class RestorePassword : AppCompatActivity() {
    private lateinit var binding: ActivityRestorePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRestorePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Boto de retornar a la pantalla de login
        binding.btnReturnMain.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        //Boto d'enviar
        binding.send.setOnClickListener {
            if (binding.dni.text.isEmpty() || binding.email.text.isEmpty()){
                Toast.makeText(this, R.string.field_empty, Toast.LENGTH_LONG).show()
            } else {
                //TODO
            }
        }


    }

}