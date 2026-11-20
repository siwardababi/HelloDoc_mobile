package tn.rnu.isetr.hellodoc.model

data class LoginResponse(
    val token: String,
    val role: String
)