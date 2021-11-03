package cat.copernic.jose.antonio.miranda.prodiscomtest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentModUserBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentValUserBinding

class valUser : Fragment() {
    private var _binding: FragmentValUserBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentValUserBinding.inflate(inflater, container, false)
        //binding.btnAddUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.addUsuario, null))

        binding.btnReturnValUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.usuarios, null))
        binding.btnValUserToHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))

        return binding.root
    }
}