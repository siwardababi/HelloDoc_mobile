package tn.rnu.isetr.hellodoc.model

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val address: String,
    val password: String,
    val consent: Boolean
)
