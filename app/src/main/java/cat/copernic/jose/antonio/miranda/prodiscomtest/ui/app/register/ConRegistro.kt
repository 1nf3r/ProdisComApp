package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.ActivityConRegistroBinding


class ConRegistro : AppCompatActivity() {
    private lateinit var binding: ActivityConRegistroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

}