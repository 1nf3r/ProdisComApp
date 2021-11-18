package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.logged

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.ActivityMainBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.ui.logged.perfil.PerfilViewModel

private lateinit var viewModel: PerfilViewModel
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        @Suppress("UNUSED_VARIABLE")
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,
            R.layout.activity_main
        )

        val extras = intent.extras
        if (extras != null) {
            viewModel = ViewModelProvider(this).get(PerfilViewModel::class.java)
            viewModel.setDni(extras.getString("DNI")!!)
            Log.d("TAG", extras.getString("DNI")!!)
            //The key argument here must match that used in the other activity
        }



        //drawerLayout = binding.drawerLayout
        //val navController = this.findNavController(R.id.visualizarFragment)
        //NavigationUI.setupActionBarWithNavController(this,navController, drawerLayout)
        //NavigationUI.setupWithNavController(binding.vistaNavegacion, navController)


    }
}