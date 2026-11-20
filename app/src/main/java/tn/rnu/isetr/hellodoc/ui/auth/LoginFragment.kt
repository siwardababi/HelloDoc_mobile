package tn.rnu.isetr.hellodoc.ui.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import tn.rnu.isetr.hellodoc.R
import tn.rnu.isetr.hellodoc.databinding.FragmentLoginBinding
import tn.rnu.isetr.hellodoc.utils.TokenManager
import tn.rnu.isetr.hellodoc.viewmodel.AuthViewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLoginBinding.bind(view)

        observeLiveData()

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Veuillez remplir tous les champs",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                authViewModel.login(email, password)
            }
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_registerFragment
            )
        }
    }

    private fun observeLiveData() {

        authViewModel.loginResult.observe(viewLifecycleOwner) { response ->

            if (response != null) {

                TokenManager.saveAuthData(
                    requireContext(),
                    response.token,
                    response.role
                )

                Toast.makeText(
                    requireContext(),
                    "Connexion réussie !",
                    Toast.LENGTH_SHORT
                ).show()

                findNavController().navigate(
                    R.id.action_loginFragment_to_homeFragment
                )
            }
        }

        authViewModel.errorMessage.observe(viewLifecycleOwner) { error ->

            if (error != null) {
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
