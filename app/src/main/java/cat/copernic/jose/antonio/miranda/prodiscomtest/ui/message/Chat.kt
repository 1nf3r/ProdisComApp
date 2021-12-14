package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import cat.copernic.jose.antonio.miranda.prodiscomtest.R
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentChatBinding
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentMensajesBinding

class Chat : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        println(ChatArgs.fromBundle(requireArguments()).main.size)

        return binding.root
    }

}