package net.packlister.packlister.api.model

import java.util.UUID

class APICategory(
    val id: UUID,
    val name: String?,
    val items: List<APICategoryItem>
)
