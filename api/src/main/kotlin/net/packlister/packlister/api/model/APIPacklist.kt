package net.packlister.packlister.api.model

import java.util.UUID

class APIPacklist(
    val id: UUID,
    val name: String?,
    val description: String?,
    val categories: List<APICategory>
)
