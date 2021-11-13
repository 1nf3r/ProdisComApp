package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.logged.perfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentPerfilBinding

private lateinit var viewModel: PerfilViewModel
class Perfil : Fragment() {
    private var _binding: FragmentPerfilBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(PerfilViewModel::class.java)
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        binding.btnToEditPerfil.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.editPerfil, null))

        binding.btnReturnPerfil.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))



        displayInfo()
        return binding.root
    }

    private fun displayInfo(){
        binding.txtDisplayNombre.setText(viewModel.nombre.value)
        binding.txtDisplayCorreo.setText(viewModel.correo.value)
        binding.txtDisplayTelefono.setText(viewModel.telefono.value.toString())
        binding.txtDisplayNacimiento.setText(viewModel.nacimiento.value)
    }

    fun getInfo(){

    }
}