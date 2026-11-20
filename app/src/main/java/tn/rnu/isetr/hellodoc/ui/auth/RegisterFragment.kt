package tn.rnu.isetr.hellodoc.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import tn.rnu.isetr.hellodoc.R
import tn.rnu.isetr.hellodoc.databinding.FragmentRegisterBinding
import tn.rnu.isetr.hellodoc.model.RegisterRequest
import tn.rnu.isetr.hellodoc.viewmodel.AuthViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(
                R.id.action_registerFragment_to_loginFragment
            )
        }

        binding.btnRegister.setOnClickListener {
            registerPatient()
        }

        observeRegistration()
    }

    private fun registerPatient() {

        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val address = binding.etAddress.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()
        val consent = binding.cbConsent.isChecked

        if (
            firstName.isEmpty() ||
            lastName.isEmpty() ||
            email.isEmpty() ||
            phone.isEmpty() ||
            address.isEmpty() ||
            password.isEmpty() ||
            confirmPassword.isEmpty()
        ) {
            Toast.makeText(
                requireContext(),
                "Veuillez remplir tous les champs.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (password != confirmPassword) {
            binding.etConfirmPassword.error = "Les mots de passe ne correspondent pas."
            return
        }

        if (firstName.length !in 2..100 || lastName.length !in 2..100) {
            Toast.makeText(
                requireContext(),
                "Le prénom et le nom doivent contenir entre 2 et 100 caractères.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Adresse e-mail invalide."
            return
        }

        if (!phone.matches(Regex("[0-9]{8,15}"))) {
            binding.etPhone.error = "Le téléphone doit contenir entre 8 et 15 chiffres."
            return
        }

        if (address.length > 300) {
            binding.etAddress.error = "L'adresse ne doit pas dépasser 300 caractères."
            return
        }

        if (password.length !in 8..72) {
            binding.etPassword.error = "Le mot de passe doit contenir entre 8 et 72 caractères."
            return
        }

        if (!consent) {
            Toast.makeText(
                requireContext(),
                "Veuillez accepter les conditions.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val request = RegisterRequest(
            firstName = firstName,
            lastName = lastName,
            email = email,
            phone = phone,
            address = address,
            password = password,
            consent = true
        )

        binding.btnRegister.isEnabled = false
        authViewModel.register(request)
    }

    private fun observeRegistration() {

        authViewModel.registerResult.observe(viewLifecycleOwner) { response ->

            if (response != null) {
                Toast.makeText(
                    requireContext(),
                    "Inscription réussie ! Vous pouvez vous connecter.",
                    Toast.LENGTH_LONG
                ).show()

                findNavController().navigate(
                    R.id.action_registerFragment_to_loginFragment
                )
            }
        }

        authViewModel.errorMessage.observe(viewLifecycleOwner) { error ->

            if (!error.isNullOrEmpty()) {
                binding.btnRegister.isEnabled = true

                Toast.makeText(
                    requireContext(),
                    error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
