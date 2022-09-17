package net.packlister.packlister.model

import java.util.UUID

data class Category(
    val id: UUID,
    val name: String?,
    val categoryItems: List<CategoryItem>
)
