package net.packlister.packlister.api.model

import java.util.UUID

data class APIUserItem(
    val id: UUID,
    val name: String?,
    val description: String?,
    val weight: Int?,
    val publicVisibility: Boolean
)
