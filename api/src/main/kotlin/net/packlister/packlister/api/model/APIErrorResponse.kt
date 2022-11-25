package net.packlister.packlister.api.model

class APIErrorResponse(
    val status: Int,
    val message: String?,
    val errors: List<APIError>?
)
