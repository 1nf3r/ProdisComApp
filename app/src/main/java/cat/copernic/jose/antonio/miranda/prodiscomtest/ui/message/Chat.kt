package cat.copernic.jose.antonio.miranda.prodiscomtest.ui.message

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import cat.copernic.jose.antonio.miranda.prodiscomtest.data.Users
import cat.copernic.jose.antonio.miranda.prodiscomtest.databinding.FragmentChatBinding

class Chat : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentChatBinding.inflate(inflater, container, false)
        println(ChatArgs.fromBundle(requireArguments()).main!!.size)
        val listOfToUsers = ChatArgs.fromBundle(requireArguments()).main

        Log.i("HOLA", ChatArgs.fromBundle(requireArguments()).main!!.size.toString())
//        Log.i("HOLA", listOfToUsers!!.toString())
//        Log.i("pasar", ChatArgs.fromBundle(requireArguments()).main!!.size.toString())

        return binding.root
    }
}