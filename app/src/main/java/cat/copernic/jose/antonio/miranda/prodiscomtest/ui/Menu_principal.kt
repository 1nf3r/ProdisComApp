package cat.copernic.jose.antonio.miranda.prodiscomtest.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentMenuPrincipalBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.viewmodel.PerfilViewModel
import com.google.firebase.auth.FirebaseAuth.*

private lateinit var viewModel: PerfilViewModel
class Menu_principal : Fragment() {

    private var _binding: FragmentMenuPrincipalBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[PerfilViewModel::class.java]
        _binding = FragmentMenuPrincipalBinding.inflate(inflater, container, false)
        binding.btnToPerfil.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.perfil,
                null
            )
        )
        binding.btnToAjustes.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.ajustes,
                null
            )
        )
        binding.btnToUsuarios.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.usuarios,
                null
            )
        )
        binding.btnToMensajes.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.mensajes,
                null
            )
        )
        binding.btnToGrup.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.grup,
                null
            )
        )
        binding.btnToSalir.setOnClickListener() {//Si clickem el boto tancar sessió...
            //Tanquem sessió
            getInstance().signOut()
            //Tornem a la pantalla login i acabem la main activity
            activity?.finish()
            activity?.startActivity(Intent(this.activity, LoginActivity::class.java))
        }

        return binding.root
    }
}