package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentMensajesBinding

class MensajesMain : Fragment() {
    private var _binding: FragmentMensajesBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMensajesBinding.inflate(inflater, container, false)
        binding.btnReturnMensajes.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.menu_principal,
                null
            )
        )
        binding.btnToMensajePers.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.chats,
                null
            )
        )
        binding.btnToContacts.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.chats,
                null
            )
        )
        return binding.root
    }

}