package cat.copernic.jose.antonio.miranda.prodiscomtest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.ActivityMainUserBinding

class MainActivityUser : AppCompatActivity() {
    private lateinit var binding: ActivityMainUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main_user
        )
    }


    //TODO Hacer que el main user pueda tener la configuracion de usuario y quitar flecha

}