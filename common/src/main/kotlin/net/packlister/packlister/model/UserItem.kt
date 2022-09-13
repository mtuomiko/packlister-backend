package net.packlister.packlister.model

import java.util.UUID

data class UserItem(
    val id: UUID,
    val name: String?,
    val description: String?,
    val weight: Int?,
    val publicVisibility: Boolean = false
)
