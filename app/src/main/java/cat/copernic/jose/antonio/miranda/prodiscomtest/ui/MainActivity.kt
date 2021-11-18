package cat.copernic.jose.antonio.miranda.prodiscomtest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.ActivityMainBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel.PerfilViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: PerfilViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        val extras = intent.extras
        if (extras != null) {
            viewModel = ViewModelProvider(this)[PerfilViewModel::class.java]
            viewModel.setDni(extras.getString("DNI")!!)
        }

    }
}