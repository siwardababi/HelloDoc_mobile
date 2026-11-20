package tn.rnu.isetr.hellodoc.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import tn.rnu.isetr.hellodoc.model.LoginRequest
import tn.rnu.isetr.hellodoc.model.LoginResponse
import tn.rnu.isetr.hellodoc.model.RegisterRequest
import tn.rnu.isetr.hellodoc.model.RegisterResponse
import tn.rnu.isetr.hellodoc.model.CsrfResponse

interface AuthApi {

    @GET("api/auth/csrf")
    suspend fun getCsrfToken(): Response<CsrfResponse>

    @POST("api/auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @POST("api/patients/registrations")
    suspend fun register(
        @Header("X-XSRF-TOKEN") csrfToken: String,
        @Body request: RegisterRequest
    ): Response<RegisterResponse>
}