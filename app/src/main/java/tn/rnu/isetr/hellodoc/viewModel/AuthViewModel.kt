
package tn.rnu.isetr.hellodoc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tn.rnu.isetr.hellodoc.model.LoginRequest
import tn.rnu.isetr.hellodoc.model.LoginResponse
import tn.rnu.isetr.hellodoc.model.RegisterRequest
import tn.rnu.isetr.hellodoc.model.RegisterResponse
import tn.rnu.isetr.hellodoc.repository.AuthRepository

class AuthViewModel : ViewModel() {

    private val repository = AuthRepository()

    // CONNEXION
    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: LiveData<LoginResponse?> = _loginResult

    // INSCRIPTION
    private val _registerResult = MutableLiveData<RegisterResponse?>()
    val registerResult: LiveData<RegisterResponse?> = _registerResult

    // ERREURS
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // CONNEXION
    fun login(email: String, motDePasse: String) {

        _errorMessage.value = null
        _loginResult.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = LoginRequest(email, motDePasse)
                val response = repository.login(request)

                _loginResult.postValue(response)

            } catch (e: Exception) {
                _errorMessage.postValue(
                    e.message ?: "Erreur de connexion. Vérifiez votre réseau."
                )
            }
        }
    }

    // INSCRIPTION
    fun register(request: RegisterRequest) {

        _errorMessage.value = null
        _registerResult.value = null

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.register(request)

                _registerResult.postValue(response)

            } catch (e: Exception) {
                _errorMessage.postValue(
                    e.message ?: "Erreur lors de l'inscription."
                )
            }
        }
    }
}
