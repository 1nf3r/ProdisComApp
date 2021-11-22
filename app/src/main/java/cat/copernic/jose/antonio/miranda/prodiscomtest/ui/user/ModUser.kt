package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentModUserBinding

class modUser : Fragment() {
    private var _binding: FragmentModUserBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentModUserBinding.inflate(inflater, container, false)

        binding.btnReturnModUser.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.usuarios, null))
        binding.btnModUserToHome.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.menu_principal, null))

        return binding.root
    }
}