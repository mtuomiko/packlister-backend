package net.packlister.packlister.dao.projection

import java.util.UUID

data class PacklistLimitedView(
    val id: UUID,
    val name: String?,
    val userId: UUID
)
