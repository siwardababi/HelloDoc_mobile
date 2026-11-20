package tn.rnu.isetr.hellodoc.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import tn.rnu.isetr.hellodoc.R
import tn.rnu.isetr.hellodoc.databinding.FragmentHomeBinding
import tn.rnu.isetr.hellodoc.utils.TokenManager

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHomeBinding.bind(view)

        val token = TokenManager.getToken(requireContext())

        if (token.isNullOrBlank()) {
            // Visiteur : affichage de l'accueil public
            binding.tvWelcome.text = "Bienvenue sur Hello Doc !"
            binding.cardNextAppointment.visibility = View.GONE
            binding.cardBookAppointment.visibility = View.GONE
            binding.cardMyAppointments.visibility = View.GONE
            binding.cardMedicalRecords.visibility = View.GONE
            binding.cardFindDoctors.visibility = View.GONE
        } else {
            // Patient connecté : affichage du tableau de bord
            binding.tvWelcome.text = "Bonjour ! 👋"
            binding.cardNextAppointment.visibility = View.VISIBLE
            binding.cardBookAppointment.visibility = View.VISIBLE
            binding.cardMyAppointments.visibility = View.VISIBLE
            binding.cardMedicalRecords.visibility = View.VISIBLE
            binding.cardFindDoctors.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
