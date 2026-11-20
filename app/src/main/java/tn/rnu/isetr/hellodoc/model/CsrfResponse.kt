package tn.rnu.isetr.hellodoc.model

data class CsrfResponse(
    val headerName: String,
    val token: String
)