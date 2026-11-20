
package tn.rnu.isetr.hellodoc.repository

import tn.rnu.isetr.hellodoc.model.LoginRequest
import tn.rnu.isetr.hellodoc.model.LoginResponse
import tn.rnu.isetr.hellodoc.model.RegisterRequest
import tn.rnu.isetr.hellodoc.model.RegisterResponse
import tn.rnu.isetr.hellodoc.network.RetrofitClient

class AuthRepository {

    suspend fun login(request: LoginRequest): LoginResponse {
        val response = RetrofitClient.authApi.login(request)

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Erreur de connexion : ${response.code()}")
        }
    }

    suspend fun register(request: RegisterRequest): RegisterResponse {

        // 1. Récupérer le token CSRF
        val csrfResponse = RetrofitClient.authApi.getCsrfToken()

        if (!csrfResponse.isSuccessful || csrfResponse.body() == null) {
            throw Exception("Impossible de récupérer le token CSRF")
        }

        val csrfToken = csrfResponse.body()!!.token

        // 2. Envoyer les informations d'inscription
        val response = RetrofitClient.authApi.register(
            csrfToken,
            request
        )

        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception(
                "Erreur d'inscription : ${response.code()} - ${response.errorBody()?.string()}"
            )
        }
    }
}
