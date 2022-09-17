package net.packlister.packlister.model

import java.util.UUID

data class Packlist(
    val id: UUID,
    val name: String?,
    val description: String?,
    val categories: List<Category>
)
