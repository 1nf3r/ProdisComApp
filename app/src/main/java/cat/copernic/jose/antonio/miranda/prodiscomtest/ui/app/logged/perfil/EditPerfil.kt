package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.app.logged.perfil

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentEditPerfilBinding

private lateinit var viewModel: PerfilViewModel
class editPerfil : Fragment() {
    private var _binding: FragmentEditPerfilBinding? = null
    private val binding get() = _binding!!
    private val per = Perfil()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(PerfilViewModel::class.java)

        _binding = FragmentEditPerfilBinding.inflate(inflater, container, false)
        binding.btnGuardarPerfil.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.perfil, null))

        binding.btnReturnEditPerfil.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.perfil, null))
        binding.btnEditePerfilToHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))

        displayInfo()
        viewModel.setInfo("","",0,"")
        return binding.root
    }

    private fun displayInfo(){
        binding.etxtNom.setText(viewModel.nombre.value)
        binding.etxtCorreu.setText(viewModel.correo.value)
        binding.etxtTelefon.setText(viewModel.telefono.value.toString())
        binding.etxtNaixement.setText(viewModel.nacimiento.value)
    }

    private fun getName(){
        //binding.etxtNom.text.toString()
    }
}